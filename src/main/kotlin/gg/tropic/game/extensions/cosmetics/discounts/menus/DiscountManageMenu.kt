package gg.tropic.game.extensions.cosmetics.discounts.menus

import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.discounts.Discount
import gg.tropic.game.extensions.cosmetics.discounts.DiscountSyncService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.buttons.AddButton
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import net.evilblock.cubed.util.time.TimeUtil
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 8/11/2024
 */
class DiscountManageMenu : PaginatedMenu()
{
    init
    {
        autoUpdate = true
        autoUpdate = true
    }

    override fun getGlobalButtons(player: Player) = mapOf(
        4 to ItemBuilder
            .copyOf(object : AddButton(){}.getButtonItem(player))
            .name("${CC.GREEN}Create Discount")
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter a resource ${CC.GRAY}(example: killeffects:*)")
                    .acceptInput { _, text ->
                        val resource = ImmutableResource.of(text)

                        val container = DiscountSyncService.cached()
                        container.discounts[resource.toString()] = Discount(resource, 50)
                        DiscountSyncService.sync(container)

                        Button.playSuccess(player)
                        player.sendMessage("${CC.GREEN}Created new discount for $resource!")
                        DiscountManageMenu().openMenu(player)
                    }
                    .start(player)
            }
    )

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()
        for ((key, discount) in DiscountSyncService.cached().discounts)
        {
            buttons[buttons.size] = ItemBuilder
                .of(Material.NAME_TAG)
                .name("${CC.GREEN}${discount.resource}")
                .addToLore(
                    "${CC.GRAY}Discount: ${CC.WHITE}${discount.percentage}%",
                    "${CC.GRAY}Active: ${CC.WHITE}${if (discount.active) "${CC.GREEN}Yes" else "${CC.RED}No"}",
                    "${CC.GRAY}Expiration:",
                    if (discount.expirationDate != null && discount.expirationDate!! > System.currentTimeMillis())
                    {
                        "${CC.WHITE}${TimeUtil.formatIntoAbbreviatedString(((discount.expirationDate!! - System.currentTimeMillis()) / 1000L).toInt())}"
                    } else
                    {
                        "${CC.RED}None!"
                    },
                    "",
                    "${CC.GREEN}Click to manage!",
                    "${CC.RED}Right-Click to delete!",
                )
                .toButton { _, type ->
                    if (type!!.isRightClick)
                    {
                        val container = DiscountSyncService.cached()
                        container.discounts.remove(key)
                        DiscountSyncService.sync(container)

                        Button.playSuccess(player)
                        player.sendMessage("${CC.GREEN}Removed discount for ${discount.resource}!")
                        DiscountManageMenu().openMenu(player)
                        return@toButton
                    }

                    Button.playNeutral(player)
                    EditDiscountMenu(key, discount).openMenu(player)
                }
        }

        return buttons
    }

    override fun getPrePaginatedTitle(player: Player) = "Managing global discounts"
}
