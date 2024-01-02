package gg.tropic.game.extensions.customtags

import com.mongodb.client.model.Filters
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.store.controller.DataStoreObjectControllerCache
import me.lucko.helper.Events
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Constants
import net.evilblock.cubed.util.bukkit.FancyMessage
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

    fun retrieveAllChatTags() = DataStoreObjectControllerCache
        .findNotNull<CustomChatTag>()
        .mongo()
        .loadAll()
        .thenApply { it.values.toList() }

    fun save(tag: CustomChatTag) = DataStoreObjectControllerCache
        .findNotNull<CustomChatTag>()
        .save(tag)

    fun retrieveExclusivelyPendingTags() = retrieveAllChatTags()
        .thenApply { tags -> tags.filter { it.verification != null } }

    @Configure
    fun configure()
    {
        Events
            .subscribe(PlayerJoinEvent::class.java)
            .handler {
                val player = it.player

                if (player.hasPermission("core.customtags.manage"))
                {
                    FancyMessage()
                        .withMessage(
                            "${CC.B_AQUA}Custom Tags ${CC.GRAY}${Constants.DOUBLE_ARROW_RIGHT} ${CC.D_AQUA}You have ${CC.AQUA}${
                                retrieveAllChatTags().join().size
                            } ${CC.D_AQUA}tags to process. ${CC.GREEN}(Click to view)"
                        )
                        .andCommandOf(
                            ClickEvent.Action.RUN_COMMAND,
                            "/managecustomtags"
                        )
                        .andHoverOf(
                            "${CC.GREEN}Click to view all pending tags"
                        )
                        .sendToPlayer(it.player)
                }
            }
    }
}