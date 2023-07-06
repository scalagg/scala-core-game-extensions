package gg.tropic.game.extensions.profile

import gg.scala.store.storage.storable.IDataStoreObject
import gg.tropic.game.extensions.profile.event.LevelUpEvent
import net.evilblock.cubed.util.CC
import java.util.*
import kotlin.math.floor
import kotlin.math.log10
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

    @JvmOverloads
    fun addCoins(
        coins: Int,
        reason: String = "",
        feedback: (String) -> Unit = {}
    )
    {
        assert(coins > 0)
        this.coins += coins

        feedback("${CC.GOLD}+$coins coins${
            if (reason.isNotBlank()) " ($reason)" else ""
        }")

        val gainedExperience = (50 * log10(coins.toDouble())).toInt()
        addExperience(gainedExperience)

        feedback("${CC.GREEN}+$gainedExperience experience${
            if (reason.isNotBlank()) " ($reason)" else ""
        }")
    }

    fun addExperience(experience: Int)
    {
        assert(experience > 0)
        this.experience += experience
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
            ).flow().subscribe()
        }
    }

    fun getXpRequiredToLevelUp() =
        getXpRequiredForLevel(level + 1) - experience

    fun getPercentageCompleteUntilNextLevel(): Float
    {
        val current = calculateExactLevelBasedOnXp()
        val previousLevel = floor(current)

        return (current - previousLevel).toFloat() * 100.0F
    }

    fun getXpRequiredForLevel(level: Int): Long
    {
        assert(level > 1)
        return (2500 * level) + 5000L
    }

    private fun calculateExactLevelBasedOnXp() =
        ((1 / 50.0) * (sqrt((2.0 * experience) + 30625.0) - 175.0)) + 1.0
}
