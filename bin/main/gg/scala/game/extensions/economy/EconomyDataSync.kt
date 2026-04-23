package gg.scala.game.extensions.economy

/**
 * Data class representing an economy configuration.
 *
 * @author Limo
 * @date 2/5/2026
 */
data class EconomyData(
    val id: String,
    val currency: Currency,
    val displayName: String = ""
)
{
    /**
     * Formats a currency amount for display.
     */
    fun format(amount: Long): String
    {
        return currency.format(amount)
    }

    /**
     * Returns the display name if set, otherwise falls back to the currency name.
     */
    fun resolvedDisplayName(): String
    {
        return if (displayName.isNullOrEmpty()) currency.name else displayName
    }

    /**
     * Creates a copy with only the display name changed.
     */
    fun withDisplayName(displayName: String) = copy(displayName = displayName)
}

/**
 * Singleton for economy data synchronization across servers.
 *
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
 * Cache containing economy configurations.
 *
 * @author Limo
 * @date 2/5/2026
 */
data class EconomyDataCache(
    val economies: MutableMap<String, EconomyData> = mutableMapOf(
        "coins" to EconomyData("coins", Currency.DEFAULT, "Default Coins")
    )
)
