package gg.scala.game.extensions.guilds

import net.evilblock.cubed.ScalaCommonsSpigot

object GuildIds
{
    private const val diff = 1L
    private const val start = 0L

    private val lock = ScalaCommonsSpigot
        .instance.kvConnection
        .sync()

    fun incrementGuid() =
        synchronized(gg.scala.game.extensions.guilds.GuildIds.lock) {
            if (gg.scala.game.extensions.guilds.GuildIds.lock.get("guild:guildid") == null)
                gg.scala.game.extensions.guilds.GuildIds.lock.set(
                    "guild:guildid",
                    gg.scala.game.extensions.guilds.GuildIds.start.toString()
                )

            return@synchronized gg.scala.game.extensions.guilds.GuildIds.lock
                .incrby("guild:guildid", gg.scala.game.extensions.guilds.GuildIds.diff)
                .toInt()
        }
}
