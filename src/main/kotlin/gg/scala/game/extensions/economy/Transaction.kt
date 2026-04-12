package gg.scala.game.extensions.economy

import java.util.UUID
import java.util.concurrent.CompletableFuture

/**
 * @author Limo
 * @date 2/5/2026
 */
enum class TransactionType
{
    Deposit,
    Withdraw,
    Transfer
}

/**
 * @author Limo
 * @date 2/5/2026
 */
data class Transaction(
    val uniqueId: UUID = UUID.randomUUID(),
    val sender: UUID,
    val receiver: UUID,
    val type: TransactionType,
    val economy: String,
    val amount: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val reason: String? = null
)

/**
 * @author Limo
 * @date 2/5/2026
 */
object TransactionService
{
    private val transactions = mutableListOf<Transaction>()

    fun submit(transaction: Transaction): CompletableFuture<Boolean>
    {
        transactions.add(transaction)

        val receiverProfile = EconomyProfileService.find(transaction.receiver)
            ?: return CompletableFuture.completedFuture(false)

        when (transaction.type)
        {
            TransactionType.Deposit -> {
                receiverProfile.addBalance(transaction.economy, transaction.amount)
            }
            TransactionType.Withdraw -> {
                receiverProfile.removeBalance(transaction.economy, transaction.amount)
            }
            TransactionType.Transfer -> {
                val senderProfile = EconomyProfileService.find(transaction.sender)
                    ?: return CompletableFuture.completedFuture(false)
                if (!senderProfile.removeBalance(transaction.economy, transaction.amount))
                {
                    return CompletableFuture.completedFuture(false)
                }
                receiverProfile.addBalance(transaction.economy, transaction.amount)
            }
        }

        return CompletableFuture.completedFuture(true)
    }

    fun getTransactionsFor(player: UUID): List<Transaction>
    {
        return transactions.filter { it.sender == player || it.receiver == player }
    }
}

/**
 * @author Limo
 * @date 2/5/2026
 */
object Accounts
{
    val SERVER: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
    val SYSTEM: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")
}
