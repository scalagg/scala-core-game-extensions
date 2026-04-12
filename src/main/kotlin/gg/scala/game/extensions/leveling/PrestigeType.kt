package gg.scala.game.extensions.leveling

/**
 * @author Limo
 * @date 2/11/2026
 */
enum class PrestigeType(val prettyName: String, val description: String)
{
    BEDWARS(
        "Bedwars",
        "Stone to Eternal, 5000+ levels"
    ),
    SKYWARS(
        "Skywars",
        "Iron to Rainbow, 70+ levels"
    ),
    DUELS(
        "Duels",
        "Rookie to Grandmaster"
    ),
    CUSTOM(
        "Custom",
        "User-defined prestige tiers"
    );

    fun next(): PrestigeType
    {
        val values = entries
        return values[(ordinal + 1) % values.size]
    }

    fun previous(): PrestigeType
    {
        val values = entries
        return values[(ordinal - 1 + values.size) % values.size]
    }
}
