package gg.scala.game.extensions.guilds

import com.mongodb.client.model.Filters
import gg.scala.basics.plugin.profile.BasicsProfile
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.type.DataStoreStorageType
import me.lucko.helper.Events
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.FancyMessage
import net.md_5.bungee.api.chat.ClickEvent
import org.bson.conversions.Bson
import org.bukkit.event.player.PlayerJoinEvent
import java.util.UUID

@Service
object GuildService
{
    @Configure
    fun configure()
    {
        Events
            .subscribe(PlayerJoinEvent::class.java)
            .handler {
                guildByUser(it.player.uniqueId)
                    .thenAccept { guild ->
                        if (guild != null)
                        {
                            return@thenAccept
                        }

                        guildsUserIsInvitedTo(it.player.uniqueId)
                            .thenAccept ctx@{ guilds ->
                                if (guilds.isEmpty())
                                {
                                    return@ctx
                                }

                                FancyMessage()
                                    .withMessage(
                                        "$prefix${CC.GREEN}You have invitations to the following guilds: ${CC.WHITE}"
                                    )
                                    .apply {
                                        guilds.values
                                            .forEachIndexed { index, guild ->
                                                withMessage("${guild.name}${
                                                    if (index != guilds.values.size - 1) ", " else ""
                                                }")
                                                andHoverOf(
                                                    "${CC.GREEN}Join the ${guild.name} guild!"
                                                )
                                                andCommandOf(
                                                    ClickEvent.Action.RUN_COMMAND,
                                                    "/guild join ${guild.name}"
                                                )
                                            }
                                    }
                                    .sendToPlayer(it.player)

                                it.player.sendMessage("$prefix${CC.I_GRAY}Click on one of the names to join the guild!")
                            }
                    }
            }
    }

    private fun findSingleWithFilter(filter: Bson) =
        DataStoreObjectControllerCache
            .findNotNull<Guild>()
            .mongo()
            .loadWithFilter(filter)

    private fun findMultipleWithFilter(filter: Bson) =
        DataStoreObjectControllerCache
            .findNotNull<Guild>()
            .mongo()
            .loadAllWithFilter(filter)

    fun destroy(guild: Guild) = DataStoreObjectControllerCache
        .findNotNull<Guild>()
        .delete(
            guild.identifier,
            DataStoreStorageType.MONGO
        )

    fun guildByName(name: String) =
        findSingleWithFilter(
            Filters.eq("name", name)
        )

    fun guildById(id: Int) =
        findSingleWithFilter(
            Filters.eq("id", id.toString())
        )

    fun guildByUser(user: UUID) =
        findSingleWithFilter(
            Filters.or(
                Filters.eq("creator.uniqueId", user.toString()),
                Filters.exists("members.$user")
            )
        )

    fun guildRequestsDisabled(user: UUID) = DataStoreObjectControllerCache
        .findNotNull<BasicsProfile>()
        .mongo()
        .loadWithFilter(
            Filters.and(
                Filters.eq("identifier", user.toString()),
                Filters.eq("settings.guilds:invites", "DISABLED")
            )
        )

    fun guildsUserIsInvitedTo(user: UUID) =
        findMultipleWithFilter(
            Filters.and(
                Filters.exists("outgoingInvitations.$user"),
                Filters.gte(
                    "outgoingInvitations.$user",
                    System.currentTimeMillis().toString()
                )
            )
        )
}
