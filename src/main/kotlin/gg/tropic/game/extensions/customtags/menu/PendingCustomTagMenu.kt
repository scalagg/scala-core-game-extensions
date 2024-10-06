package gg.tropic.game.extensions.customtags.menu

import com.cryptomorin.xseries.XMaterial
import gg.scala.lemon.util.QuickAccess
import gg.tropic.game.extensions.customtags.CustomChatTag
import gg.tropic.game.extensions.customtags.CustomChatTagService
import gg.tropic.game.extensions.customtags.Verification
import gg.tropic.game.extensions.customtags.VerificationStatus
import gg.tropic.game.extensions.profile.CorePlayerProfileService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Material
import org.bukkit.entity.Player

class PendingCustomTagMenu(private val pendingTags: List<CustomChatTag>) : PaginatedMenu()
{
    init
    {
        updateAfterClick = true
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        pendingTags
            .filter { it.verification == null }
            .forEach { tag ->
                buttons[buttons.size] = ItemBuilder.of(XMaterial.NAME_TAG)
                    .name(LegacyComponentSerializer.legacySection().serialize(tag.design))
                    .apply {
                        this.addToLore("${CC.GRAY}Creator: ${CC.WHITE}${QuickAccess.nameOrConsole(tag.ownerId)}")
                        this.addToLore(" ")
                        this.addToLore("${CC.GREEN}Left-Click to accept!")
                        this.addToLore("${CC.RED}Right-Click to reject!")
                    }
                    .toButton { _, clickType ->
                        val verification = Verification(
                            if (clickType!!.isLeftClick) VerificationStatus.Accepted else VerificationStatus.Rejected,
                            player.uniqueId,
                            System.currentTimeMillis()
                        )

                        tag.verification = verification
                        CustomChatTagService.save(tag)

                        if (clickType.isLeftClick)
                        {
                            Button.playSuccess(player)
                            player.sendMessage(
                                "${CC.GREEN}You have accepted the tag: ${
                                    LegacyComponentSerializer.legacySection().serialize(tag.design)
                                }${CC.GREEN}!"
                            )

                            QuickAccess.sendGlobalPlayerMessage(
                                "${CC.GREEN}One of your custom tags has been accepted! Use ${CC.GRAY}/customtags${CC.GREEN} to view them!",
                                tag.ownerId
                            )
                        } else
                        {
                            CorePlayerProfileService
                                .useAsyncThenSaveGlobally(tag.ownerId) { account ->
                                    account.customTagTokens = account.getCustomTagTokens() + 1
                                }
                                .thenRun {
                                    player.sendMessage(
                                        "${CC.RED}You have rejected the tag ${
                                            LegacyComponentSerializer.legacySection().serialize(tag.design)
                                        }${CC.RED}!"
                                    )
                                }
                                .thenComposeAsync {
                                    QuickAccess.sendGlobalPlayerMessage(
                                        "${CC.RED}One of your custom tags has been rejected! Use ${CC.GRAY}/customtags${CC.RED} to view which one! ${CC.BOLD}Your token has been refunded.",
                                        tag.ownerId
                                    )
                                }
                        }
                    }
            }

        return buttons
    }

    override fun getPrePaginatedTitle(player: Player): String
    {
        return "Viewing pending tags"
    }
}
