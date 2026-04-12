package gg.scala.game.extensions.economy

/**
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

    fun resolvedIcon(): String = icon ?: "GOLD_INGOT"

    fun format(amount: Long): String
    {
        return "${color}${java.text.NumberFormat.getNumberInstance().format(amount)} ${symbol}"
    }

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
