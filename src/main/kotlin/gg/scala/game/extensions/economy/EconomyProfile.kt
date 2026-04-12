package gg.scala.game.extensions.economy

import java.util.UUID

/**
 * @author Limo
 * @date 2/5/2026
 */
data class EconomyProfile(
    val uniqueId: UUID,
    private val balances: MutableMap<String, Long> = mutableMapOf()
)
{
    fun balance(currencyId: String): Long
    {
        return balances[currencyId] ?: 0L
    }

    fun setBalance(currencyId: String, amount: Long)
    {
        balances[currencyId] = amount
    }

    fun addBalance(currencyId: String, amount: Long)
    {
        balances[currencyId] = balance(currencyId) + amount
    }

    fun removeBalance(currencyId: String, amount: Long): Boolean
    {
        val current = balance(currencyId)
        if (current < amount) return false
        balances[currencyId] = current - amount
        return true
    }
}
