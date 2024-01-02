package gg.tropic.game.extensions.customtags.menu

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

class CustomTagManagementMenu(private val allTags: List<CustomChatTag>) : PaginatedMenu()
{
    init
    {
        updateAfterClick = true
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        allTags.filter { it.verification == null }.forEach { tag ->
            buttons[buttons.size] = ItemBuilder.of(Material.NAME_TAG)
                .name(LegacyComponentSerializer.legacySection().serialize(tag.design)).apply {
                    this.addToLore("${CC.GRAY}Creator: ${CC.WHITE}${QuickAccess.nameOrConsole(tag.ownerId)}")
                    this.addToLore(" ")
                    this.addToLore("${CC.GREEN}Left-Click to accept this tag")
                    this.addToLore("${CC.RED}Right-Click to reject this tag")
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
                        player.sendMessage(
                            "${CC.GREEN}You have accepted the tag ${
                                LegacyComponentSerializer.legacySection().serialize(tag.design)
                            }${CC.GREEN}!"
                        )
                    } else
                    {
                        player.sendMessage(
                            "${CC.RED}You have rejected the tag ${
                                LegacyComponentSerializer.legacySection().serialize(tag.design)
                            }${CC.RED}!"
                        )

                        val coreTargetProfile = CorePlayerProfileService.find(tag.ownerId)

                        if (coreTargetProfile == null)
                        {
                            player.sendMessage(
                                "${CC.RED}Unable to refund the custom tag token for ${CC.YELLOW}${
                                    QuickAccess.nameOrConsole(
                                        tag.ownerId
                                    )
                                }${CC.RED}."
                            )
                        } else
                        {
                            coreTargetProfile.customTagTokens = coreTargetProfile.getCustomTagTokens() + 1
                            coreTargetProfile.save()
                        }
                    }
                }
        }

        return buttons
    }

    override fun getPrePaginatedTitle(player: Player): String
    {
        return "Viewing All Custom Tags..."
    }
}