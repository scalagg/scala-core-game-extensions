package gg.scala.game.extensions.leveling

import net.evilblock.cubed.util.CC
import kotlin.math.pow

/**
 * Configurable level system supporting multiple gamemodes.
 * Each level system has its own XP strategy, prestige type, and configuration.
 *
 * @author Limo
 * @date 2/11/2026
 */
data class LevelSystem(
    val id: String,
    var prettyName: String = id.replaceFirstChar { it.uppercase() },
    var xpStrategy: XpStrategy = XpStrategy.LINEAR,
    var prestigeType: PrestigeType = PrestigeType.BEDWARS,
    var startingLevel: Int = 0,
    var startingXp: Long = 0L,
    var maxLevel: Int = 999999,
    var cycleLength: Int = 0,
    var eventsEnabled: Boolean = true,
    var negativeLevelsEnabled: Boolean = false,
    var prestigesEnabled: Boolean = false,
    var levelFormat: String = "{color}[{level}{symbol}{color}]",
    var primaryGroupChatOverrides: MutableList<String> = mutableListOf(),
    var linkedEconomy: String? = null,

    // LINEAR strategy params
    var linearXpPerLevel: Long = 1000L,

    // EXPONENTIAL strategy params
    var exponentialBaseXp: Long = 1000L,
    var exponentialMultiplier: Double = 1.1,

    // Shared param
    var minXpPerLevel: Long = 100L,

    // POLYNOMIAL strategy params
    var polynomialCoefficients: MutableList<Double> = mutableListOf(100.0, 50.0, 10.0),

    // RANGE_BASED strategy params
    var xpRanges: MutableList<XpRange> = mutableListOf()
)
{
    /**
     * Calculates XP required to advance to the specified level.
     */
    fun getXpForLevel(level: Int): Long
    {
        val raw = when (xpStrategy)
        {
            XpStrategy.BEDWARS_HYPIXEL -> getBedwarsHypixelXp(level)
            XpStrategy.LINEAR -> linearXpPerLevel
            XpStrategy.EXPONENTIAL -> getExponentialXp(level)
            XpStrategy.RANGE_BASED -> getRangeBasedXp(level)
            XpStrategy.POLYNOMIAL -> getPolynomialXp(level)
        }

        return maxOf(raw, minXpPerLevel)
    }

    /**
     * Formats the level display string using the configured format.
     * Replaces {color}, {level}, {symbol} placeholders.
     */
    fun formatLevel(level: Int, prestige: PrestigeDefinition? = null): String
    {
        val definition = prestige ?: PrestigeDefinition.forType(prestigeType)
        val tier = definition.tierForLevel(
            if (cycleLength > 0) level % cycleLength else level
        )

        return levelFormat
            .replace("{color}", tier.color)
            .replace("{level}", level.toString())
            .replace("{symbol}", tier.symbol)
    }

    /**
     * Gets the prestige definition for this level system.
     */
    fun getPrestigeDefinition(): PrestigeDefinition
    {
        return PrestigeDefinition.forType(prestigeType)
    }

    /**
     * Follows Hypixel's Bedwars XP pattern.
     * Levels within each star cycle: 500, 1000, 2000, 3500, 5000
     * repeating every 5 levels.
     */
    private fun getBedwarsHypixelXp(level: Int): Long
    {
        val adjustedLevel = if (cycleLength > 0) level % cycleLength else level
        return when (adjustedLevel % 5)
        {
            1 -> 500L
            2 -> 1000L
            3 -> 2000L
            4 -> 3500L
            0 -> 5000L
            else -> 500L
        }
    }

    /**
     * Exponential: baseXp * multiplier^(level - 1)
     */
    private fun getExponentialXp(level: Int): Long
    {
        return (exponentialBaseXp * exponentialMultiplier.pow((level - 1).coerceAtLeast(0).toDouble())).toLong()
    }

    /**
     * Evaluates polynomial: sum of (coefficient[i] * level^i)
     */
    private fun getPolynomialXp(level: Int): Long
    {
        if (polynomialCoefficients.isEmpty()) return minXpPerLevel

        var result = 0.0
        for ((i, coefficient) in polynomialCoefficients.withIndex())
        {
            result += coefficient * level.toDouble().pow(i.toDouble())
        }
        return result.toLong()
    }

    /**
     * Finds the XP range that contains the given level and returns its XP requirement.
     */
    private fun getRangeBasedXp(level: Int): Long
    {
        val range = xpRanges.firstOrNull { level in it.minLevel..it.maxLevel }
        return range?.xpRequired ?: minXpPerLevel
    }

    /**
     * Gets a summary description for display in menus.
     * Follows the exact format from the admin GUI screenshots.
     */
    fun getSummaryLore(): List<String>
    {
        val lines = mutableListOf<String>()

        lines.add("${CC.GRAY}ID: ${CC.WHITE}$id")
        lines.add("")

        val strategyDetail = when (xpStrategy)
        {
            XpStrategy.BEDWARS_HYPIXEL -> "Hypixel Bedwars"
            XpStrategy.LINEAR -> "Linear (${linearXpPerLevel} XP/level)"
            XpStrategy.EXPONENTIAL -> "Exponential (${exponentialBaseXp} base, ${exponentialMultiplier}x)"
            XpStrategy.RANGE_BASED -> "Range Based (${xpRanges.size} ranges)"
            XpStrategy.POLYNOMIAL -> "Polynomial (${polynomialCoefficients.size} coefficients)"
        }
        lines.add("${CC.GRAY}Strategy: ${CC.WHITE}$strategyDetail")

        val prestigeDetail = when (prestigeType)
        {
            PrestigeType.BEDWARS -> "Bedwars"
            PrestigeType.SKYWARS -> "Skywars"
            PrestigeType.DUELS -> "Duels"
            PrestigeType.CUSTOM -> "Custom"
        }
        lines.add("${CC.GRAY}Prestige: ${CC.WHITE}$prestigeDetail ${if (prestigesEnabled) "${CC.GREEN}Enabled" else "${CC.RED}Disabled"}")
        lines.add("")

        lines.add("${CC.GRAY}Starting Level: ${CC.WHITE}$startingLevel")
        lines.add("${CC.GRAY}Starting XP: ${CC.WHITE}$startingXp")
        lines.add("${CC.GRAY}Max Level: ${CC.WHITE}${
            if (maxLevel >= 999999) "Unlimited" else maxLevel.toString()
        }")
        lines.add("${CC.GRAY}Cycle Length: ${CC.WHITE}${
            if (cycleLength > 0) "$cycleLength levels" else "None"
        }")
        lines.add("")

        lines.add("${CC.GRAY}Events: ${if (eventsEnabled) "${CC.GREEN}Enabled" else "${CC.RED}Disabled"}")
        lines.add("${CC.GRAY}Negative Levels: ${if (negativeLevelsEnabled) "${CC.GREEN}Enabled" else "${CC.RED}Disabled"}")
        lines.add("${CC.GRAY}Format: ${CC.WHITE}$levelFormat")
        lines.add("")
        lines.add("${CC.YELLOW}Click to manage!")

        return lines
    }
}
