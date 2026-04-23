package gg.scala.game.extensions.profile

import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.storable.IDataStoreObject
import gg.scala.store.storage.type.DataStoreStorageType
import gg.scala.game.extensions.cosmetics.CosmeticTickState
import gg.scala.game.extensions.cosmetics.DynamicCosmeticConfigStore
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.customtags.CustomChatTag
import gg.scala.game.extensions.leveling.LevelSystem
import gg.scala.game.extensions.leveling.LevelSystemSyncService
import gg.scala.game.extensions.leveling.PlayerLevelData
import gg.scala.game.extensions.profile.booster.XpBooster
import gg.scala.game.extensions.profile.event.LevelUpEvent
import net.evilblock.cubed.util.CC
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * @author GrowlyX
 * @since 7/5/2023
 */
data class CorePlayerProfile(
    override val identifier: UUID,
) : IDataStoreObject
{
    var coins = 0
        private set

    var level: Int = 1
        private set

    var customTagTokens: Int? = 0
    var equippedTag: CustomChatTag? = null

    var hasVotedOnNameMC: Long? = null

    val equippedCosmetics = mutableListOf<ImmutableResource>()
    val ownedCosmetics = mutableListOf<ImmutableResource>()

    val cosmeticConfigs = mutableMapOf<String, DynamicCosmeticConfigStore>()

    @Transient
    private var backingLocalTickerConfigs: MutableMap<String, CosmeticTickState>? = null
        get()
        {
            if (field == null)
            {
                field = mutableMapOf()
                return field!!
            }

            return field!!
        }

    val localTickerConfigs: MutableMap<String, CosmeticTickState>
        get() = backingLocalTickerConfigs!!

    var experience: Long = 0L
        private set(value)
        {
            field = value
            crossCheckLevel()
        }

    var activeXpBooster: XpBooster? = null
        get()
        {
            if (field == null)
                return null

            if (field!!.expiration < System.currentTimeMillis())
                return null

            return field
        }

    val availableXpBoosters = mutableListOf<XpBooster>()

    // Per-game level tracking: levelSystemId -> PlayerLevelData
    val gameLevels: MutableMap<String, PlayerLevelData> = mutableMapOf()

    // Per-game currency balances: currencyId -> amount
    val gameBalances: MutableMap<String, Long> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    private fun mutableGameLevels(): MutableMap<String, PlayerLevelData>
    {
        val field = this::class.java.getDeclaredField("gameLevels")
        field.isAccessible = true

        val current = field.get(this) as? MutableMap<String, PlayerLevelData>
        if (current != null)
        {
            return current
        }

        val fallback = mutableMapOf<String, PlayerLevelData>()
        field.set(this, fallback)
        return fallback
    }

    @Suppress("UNCHECKED_CAST")
    private fun mutableGameBalances(): MutableMap<String, Long>
    {
        val field = this::class.java.getDeclaredField("gameBalances")
        field.isAccessible = true

        val current = field.get(this) as? MutableMap<String, Long>
        if (current != null)
        {
            return current
        }

        val fallback = mutableMapOf<String, Long>()
        field.set(this, fallback)
        return fallback
    }

    /**
     * Gets or creates level data for a specific level system.
     */
    fun getGameLevelData(levelSystemId: String): PlayerLevelData
    {
        val levels = mutableGameLevels()

        val levelData = levels.getOrPut(levelSystemId) {
            val system = LevelSystemSyncService.byId(levelSystemId)
            PlayerLevelData(
                levelSystemId = levelSystemId,
                level = system?.startingLevel ?: 0,
                experience = system?.startingXp ?: 0L
            )
        }

        LevelSystemSyncService.byId(levelSystemId)
            ?.let(levelData::crossCheckLevel)

        return levelData
    }

    /**
     * Adds experience to a specific game's level system and fires events if configured.
     */
    fun addGameExperience(
        levelSystemId: String,
        amount: Long,
        feedback: (String) -> Unit = {}
    ): CompletableFuture<Void>
    {
        val system = LevelSystemSyncService.byId(levelSystemId) ?: return CompletableFuture.completedFuture(null)
        val levelData = getGameLevelData(levelSystemId)
        val previousLevel = levelData.level

        levelData.addExperience(amount, system)

        feedback("${CC.GREEN}+$amount ${system.prettyName} experience")

        if (system.eventsEnabled && levelData.level > previousLevel)
        {
            LevelUpEvent(
                this,
                previousLevel,
                levelData.level,
                levelSystemId
            ).callEvent()
        }

        return save()
    }

    /**
     * Gets the formatted level display for a specific game level system.
     */
    fun getFormattedGameLevel(levelSystemId: String): String
    {
        val system = LevelSystemSyncService.byId(levelSystemId) ?: return "???"
        val levelData = getGameLevelData(levelSystemId)
        levelData.crossCheckLevel(system)
        return system.formatLevel(levelData.level)
    }

    /**
     * Gets level info for a specific game level system.
     */
    fun getGameLevelInfo(levelSystemId: String): LevelInfo
    {
        val system = LevelSystemSyncService.byId(levelSystemId) ?: return LevelInfo("???", 0.0, 0.0)
        val levelData = getGameLevelData(levelSystemId)

        levelData.crossCheckLevel(system)

        val rawRequired = levelData.getXpForNextLevel(system)
        val rawCurrent = levelData.getXpInCurrentLevel(system)

        val normalizedRequired = if (rawRequired <= 0L) 1L else rawRequired
        val normalizedCurrent = if (rawRequired <= 0L)
            normalizedRequired
        else
            rawCurrent.coerceIn(0L, normalizedRequired)

        return LevelInfo(
            formattedDisplay = system.formatLevel(levelData.level),
            currentXP = normalizedCurrent.toDouble(),
            xpRequiredForNext = normalizedRequired.toDouble()
        )
    }

    fun getCustomTagTokens(): Int = customTagTokens ?: 0

    /**
     * Gets the balance for a specific game currency.
     */
    fun getGameBalance(currencyId: String): Long
    {
        return mutableGameBalances()[currencyId] ?: 0L
    }

    /**
     * Adds balance for a specific game currency and persists to MongoDB.
     */
    fun addGameBalance(
        currencyId: String,
        amount: Long,
        feedback: (String) -> Unit = {}
    ): CompletableFuture<Void>
    {
        if (amount <= 0) return CompletableFuture.completedFuture(null)

        val balances = mutableGameBalances()
        balances[currencyId] = (balances[currencyId] ?: 0L) + amount

        feedback("${CC.GOLD}+$amount ${currencyId.replace("_", " ")}")

        return save()
    }

    /**
     * Removes balance for a specific game currency.
     * Returns false if the player doesn't have enough.
     */
    fun removeGameBalance(currencyId: String, amount: Long): Boolean
    {
        val balances = mutableGameBalances()
        val current = balances[currencyId] ?: 0L
        if (current < amount) return false
        balances[currencyId] = current - amount
        return true
    }

    fun activateBooster(booster: XpBooster)
    {
        availableXpBoosters.remove(booster)

        activeXpBooster = booster
        booster.activate()
    }

    fun removeCoins(coins: Int): CompletableFuture<Void>
    {
        assert(coins > 0)
        this.coins -= coins
        return save()
    }

    fun save() = DataStoreObjectControllerCache
        .findNotNull<CorePlayerProfile>()
        .save(this, DataStoreStorageType.MONGO)

    @JvmOverloads
    fun addCoins(
        coins: Int,
        reason: String = "",
        feedback: (String) -> Unit = {}
    ): CompletableFuture<Void>
    {
        assert(coins > 0)
        this.coins += coins

        feedback(
            "${CC.GOLD}+$coins coins${
                if (reason.isNotBlank()) " ($reason)" else ""
            }"
        )

        val gainedExperience = 10 * if (coins <= 300)
        {
            (150 * log10(coins.toDouble())).toInt()
        } else if (coins < 1000)
        {
            (300 * log10(coins.toDouble())).toInt()
        } else
        {
            (650 * log10(coins.toDouble())).toInt()
        }

        val scaledExperience =
            (gainedExperience * (if (activeXpBooster != null) activeXpBooster!!.multiplier else 1.0f)).roundToInt()
        addExperience(scaledExperience)

        feedback(
            "${CC.GREEN}+$scaledExperience experience${
                if (reason.isNotBlank()) " ($reason${
                    if (activeXpBooster != null) ", ${"%.2f".format(activeXpBooster!!.multiplier)}x xp booster" else ""
                })" else ""
            }"
        )

        return save()
    }

    fun addExperience(experience: Int): CompletableFuture<Void>
    {
        assert(experience > 0)
        this.experience += experience
        return save()
    }

    /**
     * Adds balance with XP rewards based on the amount and currency.
     * Used for rewarding players after games with both coins and experience.
     */
    @JvmOverloads
    fun addXPRewardedBalance(
        amount: Long,
        currency: String = "coins",
        reason: String = "",
        feedback: (String) -> Unit = {}
    ): CompletableFuture<Void>
    {
        if (amount <= 0) return CompletableFuture.completedFuture(null)

        this.coins += amount.toInt()

        feedback(
            "${CC.GOLD}+$amount $currency${
                if (reason.isNotBlank()) " ($reason)" else ""
            }"
        )

        val gainedExperience = 10 * if (amount <= 300)
        {
            (150 * log10(amount.toDouble())).toInt()
        } else if (amount < 1000)
        {
            (300 * log10(amount.toDouble())).toInt()
        } else
        {
            (650 * log10(amount.toDouble())).toInt()
        }

        val scaledExperience =
            (gainedExperience * (if (activeXpBooster != null) activeXpBooster!!.multiplier else 1.0f)).roundToInt()
        addExperience(scaledExperience)

        feedback(
            "${CC.GREEN}+$scaledExperience experience${
                if (reason.isNotBlank()) " ($reason${
                    if (activeXpBooster != null) ", ${"%.2f".format(activeXpBooster!!.multiplier)}x xp booster" else ""
                })" else ""
            }"
        )

        return save()
    }

    private fun crossCheckLevel()
    {
        val level = calculateExactLevelBasedOnXp()
        val roundedDown = floor(level)

        if (this.level < roundedDown)
        {
            val previousLevel = this.level
            this.level = roundedDown.toInt()

            LevelUpEvent(
                this,
                previousLevel, this.level
            ).callEvent()
        }
    }

    fun getPercentageCompleteUntilNextLevel(): Float
    {
        return getFractionCompleteUntilNextLevel() * 100.0F
    }

    fun getFractionCompleteUntilNextLevel(): Float
    {
        val current = calculateExactLevelBasedOnXp()
        val previousLevel = floor(current)

        return (current - previousLevel).toFloat()
    }

    fun getXpRequiredToAdvanceToLevel(level: Int): Long
    {
        assert(level > 1)
        return (2500 * level) + 5000L
    }

    private fun calculateExactLevelBasedOnXp() =
        ((1 / 50.0) * (sqrt((2.0 * experience) + 30625.0) - 175.0)) + 1.0

    /**
     * Retrieves level information for a given game context.
     */
    fun getLevelInfo(game: String): LevelInfo
    {
        if (LevelSystemSyncService.byId(game) != null)
        {
            return getGameLevelInfo(game)
        }

        val xpForNext = getXpRequiredToAdvanceToLevel(level + 1)
        val xpForCurrent = if (level > 1) getXpRequiredToAdvanceToLevel(level) else 0L
        val currentXP = experience - xpForCurrent

        return LevelInfo(
            formattedDisplay = "$level",
            currentXP = currentXP.toDouble(),
            xpRequiredForNext = (xpForNext - xpForCurrent).toDouble()
        )
    }
}
