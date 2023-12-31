package gg.tropic.game.extensions.customtags.menu

import gg.scala.commons.adventure.text.minimessage.MiniMessage
import gg.tropic.game.extensions.customtags.CustomChatTag
import gg.tropic.game.extensions.customtags.CustomChatTagService
import gg.tropic.game.extensions.customtags.Verification
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

class CustomTagsMenu(private val customChatTags: List<CustomChatTag>) : PaginatedMenu()
{
    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        customChatTags.forEach { tag ->
            buttons[buttons.size] = ItemBuilder.of(Material.NAME_TAG)
                .name(LegacyComponentSerializer.legacySection().serialize(tag.design)).apply {
                    this.addToLore(
                        "${CC.GRAY}This will set your current tag to",
                        "${CC.GRAY}the one that you created!"

                    )
                    this.addToLore(" ")
                    if (tag.verification == null)
                    {
                        this.addToLore(
                            "${CC.YELLOW}Currently pending verification!",
                        )
                    } else
                    {
                        if (tag.verification.status == Verification.VerificationStatus.Accepted)
                        {
                            this.addToLore(
                                "${CC.GREEN}Click to equip this tag!"
                            )
                        } else
                        {
                            this.addToLore(
                                "${CC.RED}Sorry, this custom tag has been rejected!"
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
            .name("${CC.B_GREEN}Redeem Custom Chat Tag")
            .addToLore(
                "${CC.WHITE}Redeems one of your custom chat tag",
                "${CC.WHITE}tokens",
                "",
                "${CC.GREEN}Click to create your custom tag!"
            )
            .toButton { _, _ ->
                player.closeInventory()

                InputPrompt()
                    .withText("${CC.GREEN}Please enter the design that you would like this chat tag to have:")
                    .acceptInput { _, input ->
                        ConfirmMenu(
                            title = "Confirm action",
                            extraInfo = listOf(
                                "${CC.GRAY}Design: ${Color.translate(input)}"
                            ),
                            confirm = true
                        ) { confirmed ->
                            if (confirmed)
                            {
                                val component = LegacyComponentSerializer.legacyAmpersand().deserialize(input)

                                CustomChatTag(ownerId = player.uniqueId, design = component).apply {
                                    CustomChatTagService.save(this)
                                }

                                player.sendMessage(
                                    "${CC.GREEN}You have redeemed a custom tag token and created the tag ${Color.translate(input)}${CC.GREEN}!"
                                )

                                player.playSound(
                                    player.location,
                                    Sound.NOTE_PLING,
                                    1.0f, 1.0f
                                )

                                player.closeInventory()
                            }
                        }.openMenu(player)
                    }.start(player)
            })
    }
}