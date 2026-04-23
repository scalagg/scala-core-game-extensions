package gg.scala.game.extensions.economy

/**
 * Represents a currency type in the economy system.
 * Each currency has a configurable icon, display name, color, symbol, and description.
 *
 * @author Limo
 * @date 2/5/2026
 */
data class Currency(
    val id: String,
    val name: String,
    val symbol: String = "$",
    val color: String = "§e",
    val description: String = "",
    val icon: String = "GOLD_INGOT"
)
{
    /**
     * Returns the resolved icon, falling back to GOLD_INGOT if null (legacy MongoDB data).
     */
    fun resolvedIcon(): String = icon ?: "GOLD_INGOT"

    /**
     * Formats a currency amount for display.
     */
    fun format(amount: Long): String
    {
        return "${color}${java.text.NumberFormat.getNumberInstance().format(amount)} ${symbol}"
    }

    /**
     * Creates a copy with only the specified field changed.
     */
    fun withName(name: String) = copy(name = name)
    fun withSymbol(symbol: String) = copy(symbol = symbol)
    fun withColor(color: String) = copy(color = color)
    fun withDescription(description: String) = copy(description = description)
    fun withIcon(icon: String) = copy(icon = icon)

    companion object
    {
        val DEFAULT = Currency(
            id = "coins",
            name = "Coins",
            symbol = "⛃",
            color = "§6",
            icon = "GOLD_INGOT"
        )
    }
}
