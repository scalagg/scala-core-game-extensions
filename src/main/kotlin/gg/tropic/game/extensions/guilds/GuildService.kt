package gg.tropic.game.extensions.guilds

import com.mongodb.client.model.Filters
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

/**
 * @author GrowlyX
 * @since 7/13/2023
 */
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
                        if (guild == null)
                        {
                            guildsUserIsInvitedTo(it.player.uniqueId)
                                .thenAccept { guilds ->
                                    if (guilds.isNotEmpty())
                                    {
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

    fun guildById(id: UUID) =
        findSingleWithFilter(
            Filters.eq("identifier", id.toString())
        )

    fun guildByUser(user: UUID) =
        findSingleWithFilter(
            Filters.or(
                Filters.eq("creator.uniqueId", user.toString()),
                Filters.exists("members.$user")
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
