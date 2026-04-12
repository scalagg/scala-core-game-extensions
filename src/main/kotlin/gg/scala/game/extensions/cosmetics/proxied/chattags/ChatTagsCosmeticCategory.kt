package gg.scala.game.extensions.cosmetics.proxied.chattags

import com.cryptomorin.xseries.XMaterial
import gg.scala.chattags.plugin.handler.ChatTagsService
import gg.scala.chattags.plugin.model.ChatTag
import gg.scala.chattags.plugin.service.ChatTagsPlayerService
import gg.scala.commons.annotations.plugin.SoftDependency
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.flavor.service.ignore.IgnoreAutoScan
import gg.scala.game.extensions.cosmetics.*
import gg.scala.game.extensions.cosmetics.proxied.ProxyCosmeticCategory
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

@Service
@IgnoreAutoScan
@SoftDependency("ScChatTags")
object ChatTagsCosmeticCategory : ProxyCosmeticCategory<ChatTag>, ProxyCosmetic
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.registerCategory(this)
    }

    override fun proxied() = ChatTagsService
        .chatTags.values.toList()

    override fun convert(t: ChatTag) = object : PurchasableCosmetic
    {
        override val price = 4500.0f

        override fun isOwnedBy(player: Player) = super.isOwnedBy(player) || ChatTagsPlayerService
            .find(player)
            ?.fixedOwnedTags
            ?.contains(t.uniqueId) == true || player.hasPermission("chattags.all-chattags")

        override fun purchase(player: Player)
        {
            val chatTagPlayer = ChatTagsPlayerService
                .find(player)
                ?: return

            super.purchase(player)

            chatTagPlayer.fixedOwnedTags.add(t.uniqueId)
            chatTagPlayer.save()
        }

        override fun equipped(player: Player) = ChatTagsPlayerService
            .find(player)
            ?.currentChatTag == t.uniqueId

        override fun setEquipped(player: Player, value: Boolean)
        {
            val chatTagPlayer = ChatTagsPlayerService
                .find(player)
                ?: return

            chatTagPlayer.currentChatTag = if (value) t.uniqueId else null
            chatTagPlayer.save()
        }

        override fun unequip(player: Player)
        {

        }

        override fun equip(player: Player)
        {

        }

        override val resource = ImmutableResource("chattags", t.uniqueId.toString())
        override val name = t.displayName
        override val description = listOf(
            "Shows in chat as:",
            "${CC.WHITE}${t.legacy}"
        )
        override val icon = ItemBuilder
            .of(XMaterial.NAME_TAG)
    }

    override val resource = ImmutableResource("chattags")
    override val name = "Chat Tags"
    override val description = listOf(
        "A cosmetic tag that is added",
        "to the end of your username",
        "when you send chat messages!"
    )

    override val icon = ItemBuilder.of(XMaterial.NAME_TAG)
}
