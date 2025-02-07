package gg.scala.game.extensions.namemc

import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.CoreGameExtensions
import gg.scala.lemon.metadata.NetworkMetadataDataSync
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.LinkedList
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