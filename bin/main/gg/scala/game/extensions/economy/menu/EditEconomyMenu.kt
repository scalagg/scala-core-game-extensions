package gg.scala.game.extensions.economy.menu

import com.cryptomorin.xseries.XMaterial
import gg.scala.game.extensions.economy.EconomyData
import gg.scala.game.extensions.economy.EconomySyncService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.Menu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import org.bukkit.entity.Player

/**
 * 5-row config menu for editing an individual economy/currency.
 * Contains all configurable options as clickable items with no borders.
 *
 * @author Limo
 * @date 2/11/2026
 */
class EditEconomyMenu(private var economy: EconomyData) : Menu(
    "Editing ${economy.resolvedDisplayName()}"
)
{
    init
    {
        autoUpdate = true
        placeholder = true
    }

    override fun size(buttons: Map<Int, Button>) = 45

    override fun getButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        // Row 1 (slots 1-6) — Economy & Currency identity
        buttons[10] = buildEconomyDisplayNameButton(player)
        buttons[11] = buildCurrencyNameButton(player)
        buttons[12] = buildSymbolButton(player)
        buttons[13] = buildColorCodeButton(player)
        buttons[14] = buildDescriptionButton(player)
        buttons[15] = buildIconButton(player)

        // Row 2 (slot 10) — Preview
        buttons[19] = buildPreviewButton()

        return buttons
    }

    /**
     * Emerald — Economy Display Name (the economy's own name, separate from currency)
     */
    private fun buildEconomyDisplayNameButton(player: Player): Button
    {
        return ItemBuilder
            .of(XMaterial.EMERALD)
            .name("${CC.YELLOW}${CC.BOLD}Economy Display Name")
            .addToLore(
                "${CC.GRAY}Name shown in admin menus.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${economy.resolvedDisplayName()}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter the display name for this economy ${CC.GRAY}(e.g. BedWars Economy)")
                    .acceptInput { _, text ->
                        economy = economy.withDisplayName(text)
                        EconomySyncService.createOrUpdate(economy)

                        player.sendMessage("${CC.GREEN}Updated economy display name to '$text'!")
                        Button.playSuccess(player)
                        EditEconomyMenu(economy).openMenu(player)
                    }
                    .start(player)
            }
    }

    /**
     * Name Tag — Currency Name (the name of the currency itself)
     */
    private fun buildCurrencyNameButton(player: Player): Button
    {
        return ItemBuilder
            .of(XMaterial.NAME_TAG)
            .name("${CC.YELLOW}${CC.BOLD}Currency Name")
            .addToLore(
                "${CC.GRAY}Name of the currency itself.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${economy.currency.name}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter the display name for this currency ${CC.GRAY}(e.g. Coins, Stars)")
                    .acceptInput { _, text ->
                        economy = economy.copy(currency = economy.currency.withName(text))
                        EconomySyncService.createOrUpdate(economy)

                        player.sendMessage("${CC.GREEN}Updated currency name to '$text'!")
                        Button.playSuccess(player)
                        EditEconomyMenu(economy).openMenu(player)
                    }
                    .start(player)
            }
    }

    /**
     * Gold Nugget — Symbol
     */
    private fun buildSymbolButton(player: Player): Button
    {
        return ItemBuilder
            .of(XMaterial.GOLD_NUGGET)
            .name("${CC.YELLOW}${CC.BOLD}Symbol")
            .addToLore(
                "${CC.GRAY}Character displayed with amounts.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${economy.currency.symbol}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter the symbol for this currency ${CC.GRAY}(e.g. \u26C3, \$, \u2727)")
                    .acceptInput { _, text ->
                        economy = economy.copy(currency = economy.currency.withSymbol(text))
                        EconomySyncService.createOrUpdate(economy)

                        player.sendMessage("${CC.GREEN}Updated symbol to '$text'!")
                        Button.playSuccess(player)
                        EditEconomyMenu(economy).openMenu(player)
                    }
                    .start(player)
            }
    }

    /**
     * Lime Dye — Color Code
     */
    private fun buildColorCodeButton(player: Player): Button
    {
        return ItemBuilder
            .of(XMaterial.LIME_DYE)
            .name("${CC.YELLOW}${CC.BOLD}Color Code")
            .addToLore(
                "${CC.GRAY}Color used for currency display.",
                "",
                "${CC.GRAY}Current: ${economy.currency.color}Sample ${CC.GRAY}(${CC.WHITE}${
                    economy.currency.color.replace("\u00a7", "&")
                }${CC.GRAY})",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter the color code ${CC.GRAY}(e.g. &6, &e, &b)")
                    .acceptInput { _, text ->
                        val colorCode = text.replace("&", "\u00a7")
                        economy = economy.copy(currency = economy.currency.withColor(colorCode))
                        EconomySyncService.createOrUpdate(economy)

                        player.sendMessage("${CC.GREEN}Updated color code to '${text}'!")
                        Button.playSuccess(player)
                        EditEconomyMenu(economy).openMenu(player)
                    }
                    .start(player)
            }
    }

    /**
     * Book — Description
     */
    private fun buildDescriptionButton(player: Player): Button
    {
        return ItemBuilder
            .of(XMaterial.BOOK)
            .name("${CC.YELLOW}${CC.BOLD}Description")
            .addToLore(
                "${CC.GRAY}Short description of this currency.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${economy.currency.description.ifEmpty { "None" }}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter a description for this currency")
                    .acceptInput { _, text ->
                        economy = economy.copy(currency = economy.currency.withDescription(text))
                        EconomySyncService.createOrUpdate(economy)

                        player.sendMessage("${CC.GREEN}Updated description!")
                        Button.playSuccess(player)
                        EditEconomyMenu(economy).openMenu(player)
                    }
                    .start(player)
            }
    }

    /**
     * Painting — Icon (chat input for material name)
     */
    private fun buildIconButton(player: Player): Button
    {
        val iconMaterial = XMaterial.matchXMaterial(economy.currency.resolvedIcon())
            .orElse(XMaterial.GOLD_INGOT)

        return ItemBuilder
            .of(iconMaterial)
            .name("${CC.YELLOW}${CC.BOLD}Icon")
            .addToLore(
                "${CC.GRAY}Material shown in menus.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${economy.currency.resolvedIcon()}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter the material name for the icon ${CC.GRAY}(e.g. GOLD_INGOT, DIAMOND, NETHER_STAR)")
                    .acceptInput { _, text ->
                        val materialName = text.uppercase().replace(" ", "_")
                        val matched = XMaterial.matchXMaterial(materialName)

                        if (!matched.isPresent)
                        {
                            player.sendMessage("${CC.RED}Invalid material '$materialName'! Please use a valid material name.")
                            Button.playFail(player)
                            EditEconomyMenu(economy).openMenu(player)
                            return@acceptInput
                        }

                        economy = economy.copy(currency = economy.currency.withIcon(materialName))
                        EconomySyncService.createOrUpdate(economy)

                        player.sendMessage("${CC.GREEN}Updated icon to '$materialName'!")
                        Button.playSuccess(player)
                        EditEconomyMenu(economy).openMenu(player)
                    }
                    .start(player)
            }
    }

    /**
     * Preview item showing formatted currency sample
     */
    private fun buildPreviewButton(): Button
    {
        val iconMaterial = XMaterial.matchXMaterial(economy.currency.resolvedIcon())
            .orElse(XMaterial.GOLD_INGOT)

        return ItemBuilder
            .of(iconMaterial)
            .name("${CC.YELLOW}${CC.BOLD}Preview")
            .addToLore(
                "${CC.GRAY}ID: ${CC.WHITE}${economy.id}",
                "${CC.GRAY}Economy: ${CC.WHITE}${economy.resolvedDisplayName()}",
                "",
                "${CC.GRAY}Currency: ${economy.currency.color}${economy.currency.name}",
                "${CC.GRAY}Symbol: ${CC.WHITE}${economy.currency.symbol}",
                "${CC.GRAY}Color: ${economy.currency.color}Sample",
                "${CC.GRAY}Icon: ${CC.WHITE}${economy.currency.resolvedIcon()}",
                "${CC.GRAY}Description: ${CC.WHITE}${economy.currency.description.ifEmpty { "None" }}",
                "",
                "${CC.GRAY}Example: ${economy.format(1250)}"
            )
            .toButton()
    }

    override fun onClose(player: Player, manualClose: Boolean)
    {
        if (manualClose)
        {
            Tasks.sync {
                ManageEconomiesMenu().openMenu(player)
            }
        }
    }
}
