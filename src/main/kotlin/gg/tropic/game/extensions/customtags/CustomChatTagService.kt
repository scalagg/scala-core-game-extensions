package gg.tropic.game.extensions.customtags

import com.mongodb.client.model.Filters
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.lemon.channel.channels.DefaultChatChannel
import gg.scala.lemon.listener.PlayerListener
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.tropic.game.extensions.profile.CorePlayerProfileService
import me.lucko.helper.Events
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Constants
import net.evilblock.cubed.util.bukkit.FancyMessage
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.ClickEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

@Service
object CustomChatTagService
{
    fun getChatTagsFromPlayer(target: UUID) = DataStoreObjectControllerCache
        .findNotNull<CustomChatTag>()
        .mongo()
        .loadAllWithFilter(
            Filters.eq("ownerId", target.toString())
        )
        .thenApply { it.values.toList() }

    fun getAllPendingChatTags() = DataStoreObjectControllerCache
        .findNotNull<CustomChatTag>()
        .mongo()
        .loadAllWithFilter(
            Filters.eq("verification", null)
        )
        .thenApply { it.values.toList() }

    fun save(tag: CustomChatTag) = DataStoreObjectControllerCache
        .findNotNull<CustomChatTag>()
        .save(tag)

    @Configure
    fun configure()
    {
        val previousProvider = DefaultChatChannel.chatTagProvider
        DefaultChatChannel.chatTagProvider = provider@{
            val coreProfile = CorePlayerProfileService.find(it)
                ?: return@provider previousProvider(it)

            if (coreProfile.equippedTag == null)
            {
                return@provider previousProvider(it)
            }

            Component.text(" ")
                .append(coreProfile.equippedTag!!.design)
        }

        Events
            .subscribe(PlayerJoinEvent::class.java)
            .handler {
                val player = it.player

                if (player.hasPermission("core.customtags.manage"))
                {
                    getAllPendingChatTags()
                        .thenAccept { pending ->
                            if (pending.isEmpty())
                            {
                                return@thenAccept
                            }

                            FancyMessage()
                                .withMessage(
                                    "${CC.B_AQUA}Custom Tags ${CC.GRAY}${Constants.DOUBLE_ARROW_RIGHT} ${CC.D_AQUA}You have ${CC.AQUA}${pending.size} ${CC.D_AQUA}tag${
                                        if (pending.size == 1) "" else "s"
                                    } to process. ${CC.GREEN}(Click to view)"
                                )
                                .andCommandOf(
                                    ClickEvent.Action.RUN_COMMAND,
                                    "/managecustomtags"
                                )
                                .andHoverOf(
                                    "${CC.GREEN}Click to view all pending tags."
                                )
                                .sendToPlayer(it.player)
                        }
                }
            }
    }
}
