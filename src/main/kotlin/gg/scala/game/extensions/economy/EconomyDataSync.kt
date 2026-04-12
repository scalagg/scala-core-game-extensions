package gg.scala.game.extensions.economy

/**
 * @author Limo
 * @date 2/5/2026
 */
data class EconomyData(
    val id: String,
    val currency: Currency,
    val displayName: String = ""
)
{

    fun format(amount: Long): String
    {
        return currency.format(amount)
    }

    fun resolvedDisplayName(): String
    {
        return if (displayName.isNullOrEmpty()) currency.name else displayName
    }

    fun withDisplayName(displayName: String) = copy(displayName = displayName)
}

/**
 * @author Limo
 * @date 2/5/2026
 */
object EconomyDataSync
{
    private var cachedData: EconomyDataCache = EconomyDataCache()

    fun cached(): EconomyDataCache = cachedData

    fun reload(cache: EconomyDataCache)
    {
        cachedData = cache
    }
}

/**
 * @author Limo
 * @date 2/5/2026
 */
data class EconomyDataCache(
    val economies: MutableMap<String, EconomyData> = mutableMapOf(
        "coins" to EconomyData("coins", Currency.DEFAULT, "Default Coins")
    )
)
