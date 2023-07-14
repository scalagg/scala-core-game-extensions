package gg.tropic.game.extensions.profile

import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.storable.IDataStoreObject
import gg.scala.store.storage.type.DataStoreStorageType
import gg.tropic.game.extensions.profile.booster.XpBooster
import gg.tropic.game.extensions.profile.event.LevelUpEvent
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

        feedback("${CC.GOLD}+$coins coins${
            if (reason.isNotBlank()) " ($reason)" else ""
        }")

        val gainedExperience = if (coins <= 300)
        {
            (150 * log10(coins.toDouble())).toInt()
        } else if (coins < 1000)
        {
            (300 * log10(coins.toDouble())).toInt()
        } else
        {
            (650 * log10(coins.toDouble())).toInt()
        }

        addExperience(
            (gainedExperience * if (activeXpBooster != null) activeXpBooster!!.multiplier else 1.0f).roundToInt()
        )

        feedback("${CC.GREEN}+$gainedExperience experience${
            if (reason.isNotBlank()) " ($reason${
                if (activeXpBooster != null) ", ${"%.2f".format(activeXpBooster!!.multiplier)}x xp booster" else ""
            })" else ""
        }")

        return save()
    }

    fun addExperience(experience: Int): CompletableFuture<Void>
    {
        assert(experience > 0)
        this.experience += experience
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

    fun getXpRequiredToLevelUp(): Long
    {
        TODO("issue")
    }

    fun getPercentageCompleteUntilNextLevel(): Float
    {
        val current = calculateExactLevelBasedOnXp()
        val previousLevel = floor(current)

        return (current - previousLevel).toFloat() * 100.0F
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
}
