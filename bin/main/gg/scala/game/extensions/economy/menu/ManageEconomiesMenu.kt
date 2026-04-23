package gg.scala.game.extensions.economy.menu

import com.cryptomorin.xseries.XMaterial
import gg.scala.game.extensions.economy.Currency
import gg.scala.game.extensions.economy.EconomyData
import gg.scala.game.extensions.economy.EconomySyncService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import org.bukkit.entity.Player

/**
 * Admin GUI for managing all registered economy/currency types.
 * Uses a paginated layout with WRITABLE_BOOK create button.
 *
 * @author Limo
 * @date 2/11/2026
 */
class ManageEconomiesMenu : PaginatedMenu()
{
    init
    {
        autoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player) = "Managing Economies"

    override fun getGlobalButtons(player: Player) = mapOf(
        4 to ItemBuilder
            .of(XMaterial.WRITABLE_BOOK)
            .name("${CC.GREEN}${CC.BOLD}Create Economy")
            .addToLore(
                "${CC.GRAY}Click to create a new economy type."
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter a unique ID for the economy ${CC.GRAY}(e.g. bedwars_coins)")
                    .acceptInput { _, text ->
                        val id = text.lowercase().replace(" ", "_")

                        if (EconomySyncService.economyById(id) != null)
                        {
                            player.sendMessage("${CC.RED}An economy with ID '$id' already exists!")
                            Button.playFail(player)
                            ManageEconomiesMenu().openMenu(player)
                            return@acceptInput
                        }

                        val economy = EconomyData(
                            id = id,
                            currency = Currency(
                                id = id,
                                name = text.replaceFirstChar { it.uppercase() }
                            ),
                            displayName = text.replaceFirstChar { it.uppercase() }
                        )
                        EconomySyncService.createOrUpdate(economy)

                        player.sendMessage("${CC.GREEN}Created economy '$id'!")
                        Button.playSuccess(player)
                        EditEconomyMenu(economy).openMenu(player)
                    }
                    .start(player)
            }
    )

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        for ((_, economy) in EconomySyncService.all())
        {
            val iconMaterial = XMaterial.matchXMaterial(economy.currency.resolvedIcon())
                .orElse(XMaterial.GOLD_INGOT)

            val lore = mutableListOf(
                "${CC.GRAY}ID: ${CC.WHITE}${economy.id}",
                "",
                "${CC.GRAY}Currency: ${economy.currency.color}${economy.currency.name}",
                "${CC.GRAY}Symbol: ${CC.WHITE}${economy.currency.symbol}",
                "${CC.GRAY}Color: ${economy.currency.color}Sample",
                "${CC.GRAY}Icon: ${CC.WHITE}${economy.currency.resolvedIcon()}"
            )

            if (economy.currency.description.isNotEmpty())
            {
                lore.add("${CC.GRAY}Description: ${CC.WHITE}${economy.currency.description}")
            }

            lore.add("")
            lore.add("${CC.GRAY}Example: ${economy.format(1250)}")
            lore.add("")
            lore.add("${CC.YELLOW}Left-Click to edit!")
            lore.add("${CC.YELLOW}Right-Click to delete!")

            buttons[buttons.size] = ItemBuilder
                .of(iconMaterial)
                .name("${CC.YELLOW}${CC.BOLD}${economy.resolvedDisplayName()}")
                .setLore(lore)
                .toButton { _, type ->
                    if (type!!.isRightClick)
                    {
                        EconomySyncService.delete(economy.id)
                        player.sendMessage("${CC.GREEN}Deleted economy '${economy.id}'!")
                        Button.playSuccess(player)
                        ManageEconomiesMenu().openMenu(player)
                        return@toButton
                    }

                    Button.playNeutral(player)
                    EditEconomyMenu(economy).openMenu(player)
                }
        }

        return buttons
    }
}
