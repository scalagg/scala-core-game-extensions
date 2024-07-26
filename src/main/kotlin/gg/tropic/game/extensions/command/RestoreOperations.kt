package gg.tropic.game.extensions.command

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import net.evilblock.cubed.serializers.Serializers
import net.evilblock.cubed.util.bukkit.uuid.UUIDUtil
import org.bukkit.Bukkit
import org.bukkit.command.ConsoleCommandSender
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * @author GrowlyX
 * @since 7/26/2024
 */
//@AutoRegister
object RestoreOperations : ScalaCommand()
{
    data class PaymentData(
        val current_page: Int,
        val data: List<Payment>,
        val first_page_url: String,
        val from: Int,
        val last_page: Int,
        val last_page_url: String,
        val next_page_url: String?,
        val path: String,
        val per_page: Int,
        val prev_page_url: String?,
        val to: Int,
        val total: Int
    )

    data class Payment(
        val id: Long,
        val amount: String,
        val date: String,
        val gateway: Gateway,
        val status: String,
        val currency: Currency,
        val email: String,
        val player: Player,
        val packages: List<Package>,
        val notes: List<Note>,
        val creator_code: String?
    )

    data class Note(
        val created_at: String,
        val note: String
    )

    data class Gateway(
        val id: Int,
        val name: String
    )

    data class Currency(
        val iso_4217: String,
        val symbol: String
    )

    data class Player(
        val id: Long,
        val name: String,
        val uuid: String
    )

    data class Package(
        val quantity: Int,
        val id: Long,
        val name: String
    )

    val ranks = listOf("VIP", "VIP+", "MVP", "ESTA")
    val upgrades = mapOf("VIP ➔ VIP+" to "VIP+", "VIP+ ➔ MVP" to "MVP")
    val tags = """
Verified
Love
Star
YinYang
Lightning
Dropped
 
Money
 
L
 
Weed
 
First
 
69
 
GG
 
EZ
 
Daddy
 
Vape
 
Abuse
 
Rage
 
Fake
 
Sexy
 
AFK
 
Rekt
 
Toxic
 
Savage
 
IQ200
 
Scam
 
Hacker
 
BestEU
 
Quicked
 
Salty
 
Bat
Halloween
DDOS
Epic
Covid-19
LuckyBlock
E-Girl
Flex
Banned
God
Vibes
Eyes
Gucci
Diamond
Bear
Dope
Among Us
Sus
    """
    val actualTags = tags.split("\n")
        .filter { it.isNotBlank() }


    @CommandAlias("restoreoperations")
    @CommandPermission("op")
    fun cmd(player: ConsoleCommandSender)
    {
        thread {
            var nextURL: String? = "https://plugin.tebex.io/payments?paged=1"
            while (nextURL != null)
            {
                player.sendMessage("Polling data from $nextURL")

                val data = Serializers.gson.fromJson(fetchData(nextURL, "X-Tebex-Secret", secretKey), PaymentData::class.java)
                nextURL = data.next_page_url

                val completeTxns = data.data.filter { it.status == "Complete" }
                for (completeTxn in completeTxns)
                {
                    val playerID = UUIDUtil.formatUUID(completeTxn.player.uuid)
                    player.sendMessage("Processing data for $playerID")
                    completeTxn.packages.filter { it.name in ranks }
                        .forEach {
                            player.sendMessage("Player deserves ${it.name}")
                            Bukkit.dispatchCommand(
                                player,
                                "grantmanual $playerID ${it.name} perm Webstore Purchase"
                            )
                        }

                    completeTxn.packages.filter { upgrades[it.name] != null }
                        .forEach {
                            val mapping = upgrades[it.name]
                            player.sendMessage("Player deserves ${it.name} UPGRADE")
                            Bukkit.dispatchCommand(
                                player,
                                "grantmanual $playerID $mapping Webstore Purchase"
                            )
                        }

                    completeTxn.packages.filter { it.name in actualTags }
                        .forEach {
                            player.sendMessage("Player deserves ${it.name} UPGRADE")
                            Bukkit.dispatchCommand(
                                player,
                                "cta add $playerID ${it.name}"
                            )
                        }
                }
            }
        }
    }
}

fun fetchData(urlString: String, headerName: String, headerValue: String): String
{
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection

    return try
    {
        connection.requestMethod = "GET"
        connection.setRequestProperty(headerName, headerValue)
        connection.connect()

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            connection.inputStream.bufferedReader().use { it.readText() }
        } else
        {
            throw RuntimeException("Failed : HTTP error code : $responseCode")
        }
    } finally
    {
        connection.disconnect()
    }
}
