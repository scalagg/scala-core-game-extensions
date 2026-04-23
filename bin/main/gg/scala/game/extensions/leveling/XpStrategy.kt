package gg.scala.game.extensions.leveling

/**
 * Defines how XP requirements scale per level within a level system.
 *
 * @author Limo
 * @date 2/11/2026
 */
enum class XpStrategy(val prettyName: String, val description: String)
{
    BEDWARS_HYPIXEL(
        "Bedwars Hypixel",
        "Follows Hypixel's Bedwars XP pattern"
    ),
    LINEAR(
        "Linear",
        "Same XP required per level"
    ),
    EXPONENTIAL(
        "Exponential",
        "XP grows exponentially per level"
    ),
    RANGE_BASED(
        "Range Based",
        "Custom XP ranges with min/max levels"
    ),
    POLYNOMIAL(
        "Polynomial",
        "Uses polynomial coefficients for XP scaling"
    );

    /**
     * Cycles to the next strategy in the enum.
     */
    fun next(): XpStrategy
    {
        val values = entries
        return values[(ordinal + 1) % values.size]
    }

    /**
     * Cycles to the previous strategy in the enum.
     */
    fun previous(): XpStrategy
    {
        val values = entries
        return values[(ordinal - 1 + values.size) % values.size]
    }
}
