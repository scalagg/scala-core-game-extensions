package gg.tropic.game.extensions.customtags.menu

import gg.scala.lemon.LemonConstants
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
import org.bukkit.ChatColor
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
        val profile = CorePlayerProfileService.find(player)
            ?: return buttons

        customChatTags
            .sortedBy {
                if (it.verification != null && it.verification!!.status == VerificationStatus.Accepted)
                    0 else 1
            }
            .forEach { tag ->
                buttons[buttons.size] = ItemBuilder
                    .of(
                        if (
                            tag.verification != null &&
                            tag.verification!!.status == VerificationStatus.Rejected
                        )
                            Material.COAL_BLOCK
                        else
                            Material.NAME_TAG
                    )
                    .apply {
                        if (profile.equippedTag?.identifier == tag.identifier)
                            glow()
                    }
                    .name(
                        LegacyComponentSerializer
                            .legacySection()
                            .serialize(tag.design)
                    )
                    .apply {
                        this.addToLore(
                            "${CC.GRAY}This is a custom tag you",
                            "${CC.GRAY}have created!",
                            ""
                        )

                        if (tag.verification == null)
                        {
                            this.addToLore(
                                "${CC.YELLOW}Pending verification!",
                            )
                        } else
                        {
                            if (tag.verification!!.status == VerificationStatus.Accepted)
                            {
                                if (profile.equippedTag?.identifier == tag.identifier)
                                {
                                    addToLore("${CC.GREEN}Click to un-equip!")
                                    return@apply
                                }

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
                    .toButton { _, _ ->
                        if (tag.verification!!.status == VerificationStatus.Accepted)
                        {
                            if (profile.equippedTag?.identifier == tag.identifier)
                            {
                                profile.equippedTag = null
                                profile.save()

                                Button.playNeutral(player)
                                openMenu(player)
                                return@toButton
                            }

                            profile.equippedTag = tag
                            profile.save()

                            Button.playNeutral(player)
                            openMenu(player)
                        }
                    }
            }
        return buttons
    }


    override fun getPrePaginatedTitle(player: Player): String
    {
        return "Viewing your custom tags"
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button>
    {
        val tagTokens = CorePlayerProfileService.find(player)?.getCustomTagTokens() ?: 0

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
                if (tagTokens == 0)
                    "${CC.RED}Purchase tokens at: ${CC.B_WHITE}${LemonConstants.STORE_LINK}"
                else
                    "${CC.WHITE}Tokens: ${CC.PRI}$tagTokens",
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

                InputPrompt()
                    .withText("${CC.GREEN}Please enter the design that you would like this chat tag to have:")
                    .acceptInput { _, input ->
                        if (ChatColor.stripColor(input).length >= 16)
                        {
                            player.sendMessage("${CC.RED}Your chat tag must not be greater than 16 characters long!")
                            return@acceptInput
                        }

                        if (ChatMessageFilterHandler.handleMessageFilter(
                                player = player,
                                message = input,
                                reportToStaff = false
                            )
                        )
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
                                    player.sendMessage(
                                        "${CC.GREEN}You have requested the chat tag: ${
                                            Color.translate(
                                                input
                                            )
                                        }"
                                    )
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
