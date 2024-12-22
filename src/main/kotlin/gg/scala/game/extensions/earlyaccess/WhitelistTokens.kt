package gg.scala.game.extensions.earlyaccess

import net.evilblock.cubed.ScalaCommonsSpigot
import java.util.UUID
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 7/12/2023
 */
object WhitelistTokens
{
    fun of(user: UUID) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hget(
                    "whitelist-tokens:tokens",
                    user.toString()
                )
                ?.toIntOrNull()
        }


    fun isWhitelisted(user: UUID) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hexists(
                    "whitelist-tokens:whitelisted",
                    user.toString()
                )
        }
}
