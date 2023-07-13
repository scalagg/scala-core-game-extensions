package gg.tropic.game.extensions.guilds

import net.evilblock.cubed.ScalaCommonsSpigot

/**
 * @author GrowlyX
 * @since 7/4/2023
 */
object GuildIds
{
    private const val diff = 1L
    private const val start = 0L

    private val lock = ScalaCommonsSpigot
        .instance.kvConnection
        .sync()

    fun guid() = lock.get("guild:guildid")

    fun incrementGuid() =
        synchronized(lock) {
            if (lock.get("guild:guildid") == null)
                lock.set(
                    "guild:guildid",
                    start.toString()
                )

            return@synchronized lock
                .incrby("guild:guildid", diff)
                .toInt()
        }
}
