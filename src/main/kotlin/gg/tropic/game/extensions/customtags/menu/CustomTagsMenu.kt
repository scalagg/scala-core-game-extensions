package gg.tropic.game.extensions.customtags.menu

import gg.scala.lemon.filter.ChatMessageFilterHandler
import gg.tropic.game.extensions.customtags.CustomChatTag
import gg.tropic.game.extensions.customtags.CustomChatTagService
import gg.tropic.game.extensions.customtags.VerificationStatus
import gg.tropic.game.extensions.profile.CorePlayerProfileService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.buttons.AddButton
import net.evilblock.cubed.menu.menus.ConfirmMenu
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.Color
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player

class CustomTagsMenu(
    private val customChatTags: List<CustomChatTag>
) : PaginatedMenu()
{
    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        customChatTags.forEach { tag ->
            buttons[buttons.size] = ItemBuilder
                .of(
                    if (
                        tag.verification != null &&
                        tag.verification.status == VerificationStatus.Rejected
                    )
                        Material.COAL
                    else
                        Material.NAME_TAG
                )
                .name(
                    LegacyComponentSerializer
                        .legacySection()
                        .serialize(tag.design)
                )
                .apply {
                    this.addToLore(
                        "${CC.GRAY}This will set your current tag to",
                        "${CC.GRAY}the one that you created!",
                        ""
                    )

                    if (tag.verification == null)
                    {
                        this.addToLore(
                            "${CC.YELLOW}Pending verification!",
                        )
                    } else
                    {
                        if (tag.verification.status == VerificationStatus.Accepted)
                        {
                            this.addToLore(
                                "${CC.GREEN}Click to equip!"
                            )
                        } else
                        {
                            this.addToLore(
                                "${CC.RED}Sorry, this custom tag has",
                                "${CC.RED}been rejected by staff.",
                                "",
                                "${CC.B_RED}You have been refunded your",
                                "${CC.B_RED}token."
                            )
                        }
                    }
                }
                .toButton()
        }

        return buttons
    }

    override fun getPrePaginatedTitle(player: Player): String
    {
        return "Viewing Your Custom Tags..."
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button>?
    {
        return hashMapOf(4 to ItemBuilder
            .copyOf(
                object : AddButton()
                {}
                    .getButtonItem(player)
            )
            .name("${CC.GREEN}Redeem Custom Chat Tag")
            .addToLore(
                "${CC.GRAY}Redeems one of your custom chat tag",
                "${CC.GRAY}tokens!",
                "",
                "${CC.GREEN}Click to create!"
            )
            .toButton { _, _ ->
                val profile = CorePlayerProfileService.find(player)
                    ?: return@toButton

                if (profile.getCustomTagTokens() == 0)
                {
                    player.sendMessage("${CC.RED}You do not have any custom chat tokens to redeem!")
                    Button.playFail(player)
                    return@toButton
                }

                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("${CC.GREEN}Please enter the design that you would like this chat tag to have:")
                    .acceptInput { _, input ->
                        if (input.length >= 16)
                        {
                            player.sendMessage("${CC.RED}Your chat tag must not be greater than 16 characters long!")
                            return@acceptInput
                        }

                        if (ChatMessageFilterHandler.handleMessageFilter(
                            player = player,
                            message = input,
                            reportToStaff = false
                        ))
                        {
                            player.sendMessage("${CC.RED}Your chat tag must not contain any profanity!")
                            return@acceptInput
                        }

                        ConfirmMenu(
                            title = "Confirm registration",
                            extraInfo = listOf(
                                "${CC.GRAY}Design: ${Color.translate(input)}"
                            ),
                            confirm = true
                        ) { confirmed ->
                            if (confirmed)
                            {
                                val coreProfile = CorePlayerProfileService.find(player)
                                    ?: return@ConfirmMenu

                                val component = LegacyComponentSerializer
                                    .legacyAmpersand().deserialize(input)

                                CustomChatTag(
                                    ownerId = player.uniqueId,
                                    design = component
                                ).apply {
                                    CustomChatTagService.save(this)
                                }

                                coreProfile.customTagTokens = (coreProfile.customTagTokens ?: 1) - 1
                                coreProfile.save().thenRun {
                                    player.sendMessage("${CC.GREEN}You have requested the chat tag: ${Color.translate(input)}")
                                    player.sendMessage("${CC.GRAY}Note: it may take up to 24 hours for a staff member to verify your chat tag.")

                                    player.playSound(
                                        player.location,
                                        Sound.NOTE_PLING,
                                        1.0f, 1.0f
                                    )
                                }

                                player.closeInventory()
                            }
                        }.openMenu(player)
                    }.start(player)
            })
    }
}
