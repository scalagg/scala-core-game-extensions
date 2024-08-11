package gg.tropic.game.extensions.cosmetics.discounts.menus

import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.discounts.Discount
import gg.tropic.game.extensions.cosmetics.discounts.DiscountSyncService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.Menu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import net.evilblock.cubed.util.bukkit.prompt.NumberPrompt
import net.evilblock.cubed.util.time.Duration
import net.evilblock.cubed.util.time.TimeUtil
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 8/11/2024
 */
class EditDiscountMenu(private val resource: String, private val discount: Discount) : Menu(
    "Editing discount $resource"
)
{
    init
    {
        placeholder = true
    }

    override fun size(buttons: Map<Int, Button>) = 27
    override fun getButtons(player: Player) = mapOf(
        10 to ItemBuilder
            .of(Material.GOLD_INGOT)
            .name("${CC.YELLOW}Percentage: ${CC.WHITE}${discount.percentage}")
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                NumberPrompt()
                    .withText("Enter a percentage ${CC.GRAY}(example: 60)")
                    .acceptInput { number ->
                        val container = DiscountSyncService.cached()
                        discount.percentage = number.toInt()

                        container.discounts[resource] = discount
                        DiscountSyncService.sync(container)

                        Button.playSuccess(player)
                        player.sendMessage("${CC.GREEN}Updatde percentage for $resource to $number!")
                        EditDiscountMenu(resource, discount).openMenu(player)
                    }
                    .start(player)
            },
        11 to ItemBuilder
            .of(Material.MAP)
            .name("${CC.YELLOW}Active: ${CC.WHITE}${
                if (discount.active) "${CC.GREEN}Yes" else "${CC.RED}No"
            }")
            .toButton { _, _ ->
                val container = DiscountSyncService.cached()
                discount.active = !discount.active

                container.discounts[resource] = discount
                DiscountSyncService.sync(container)
                openMenu(player)
            },
        12 to ItemBuilder
            .of(Material.WATCH)
            .name("${CC.YELLOW}Expiration: ${CC.WHITE}${
                if (discount.expirationDate != null)
                {
                    "${CC.WHITE}${TimeUtil.formatIntoAbbreviatedString(((System.currentTimeMillis() - discount.expirationDate!!) / 1000L).toInt())}"
                } else
                {
                    "${CC.RED}None!"
                }
            }")
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter a duration ${CC.GRAY}(example: 5d6m40s)")
                    .acceptInput { _, text ->
                        val duration = Duration.parse(text)
                        val container = DiscountSyncService.cached()
                        if (!duration.isPermanent())
                        {
                            discount.expirationDate = System.currentTimeMillis() + duration.get()
                        } else
                        {
                            discount.expirationDate = null
                        }

                        container.discounts[resource] = discount
                        DiscountSyncService.sync(container)

                        Button.playSuccess(player)
                        player.sendMessage("${CC.GREEN}Update expiration for $resource to $text!")
                        EditDiscountMenu(resource, discount).openMenu(player)
                    }
                    .start(player)
            }
    )

    override fun onClose(player: Player, manualClose: Boolean)
    {
        if (manualClose)
        {
            Tasks.sync {
                DiscountManageMenu().openMenu(player)
            }
        }
    }
}
