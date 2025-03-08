package gg.scala.game.extensions.namemc

import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.CoreGameExtensions
import gg.scala.lemon.metadata.NetworkMetadataDataSync
import net.evilblock.cubed.util.CC
import okhttp3.OkHttpClient
import okhttp3.Request
import org.bukkit.Bukkit
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.util.LinkedList
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Topu
 * @date 2/7/2025
 */
@Service
object NameMCService : Runnable
{
    @Inject
    lateinit var plugin: CoreGameExtensions

    val queue = LinkedList<NameMCVoteRequest>()
    val executor = Executors.newSingleThreadScheduledExecutor()

    val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    @Configure
    fun configure()
    {
        executor.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.SECONDS)
    }

    fun submit(request: NameMCVoteRequest)
    {
        queue.addLast(request)
    }

    fun fetchLikes(): String {
        val serverIp = NetworkMetadataDataSync.cached().domain
        val apiUrl = "https://api.namemc.com/server/$serverIp/likes"

        return try {
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
            connection.setRequestProperty("Accept", "application/json")

            val response = connection.inputStream.bufferedReader().use { it.readText() }
            connection.disconnect()

            val jsonResponse = JSONArray(response)
            "${CC.WHITE}${jsonResponse.length()}"
        } catch (e: Exception) {
            e.printStackTrace()
            "${CC.WHITE}0"
        }
    }

    override fun run()
    {
        val cached = NameMCDataSync.cached()
        while (queue.isNotEmpty())
        {
            val item = queue.pop()
            val request = Request.Builder()
                .url(
                    cached.targetUrl.format(
                        NetworkMetadataDataSync.cached().domain,
                        item.uniqueId
                    )
                )
                .get()
                .build()

            val hasVoted = client.newCall(request)
                .execute()
                .body
                ?.string()
                ?.toBooleanStrictOrNull()
                ?: false

            item.future(hasVoted)
        }

    }
}