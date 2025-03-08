package gg.scala.game.extensions.discord

import gg.scala.flavor.service.Service
import net.evilblock.cubed.util.CC
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Topu
 * @date 3/8/2025
 */
@Service
object DiscordService
{

    fun fetchDiscordMembers(): String {
        val guildId = "999774017223409714"
        val apiUrl = "https://discord.com/api/guilds/$guildId/widget.json"

        return try {
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
            connection.setRequestProperty("Accept", "application/json")

            val response = connection.inputStream.bufferedReader().use { it.readText() }
            connection.disconnect()

            val jsonResponse = JSONObject(response)
            val memberCount = jsonResponse.getInt("presence_count") // Online members

            "${CC.WHITE}$memberCount"
        } catch (e: Exception) {
            e.printStackTrace()
            "${CC.WHITE}0"
        }
    }
}