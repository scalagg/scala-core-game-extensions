package gg.tropic.game.extensions.cosmetics

import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.menus.ConfirmMenu
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.Sound
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
class CosmeticBrowseMenu(
    private val category: CosmeticCategory
) : PaginatedMenu()
{
    init
    {
        placeholdBorders = true
        updateAfterClick = true
    }

    override fun size(buttons: Map<Int, Button>) = 36
    override fun getMaxItemsPerPage(player: Player) = 14

    override fun getAllPagesButtonSlots() = (10..16) + (19..25)
    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        category.cosmetics()
            .forEach {
                buttons[buttons.size] = it.create()
                    .addToLore("")
                    .apply {
                        val equipped = it.equipped(player)
                        if (it is PurchasableCosmetic)
                        {
                            if (it.isOwnedBy(player))
                            {
                                addToLore(
                                    "${CC.GREEN}You own this!"
                                )
                            } else
                            {
                                addToLore(
                                    "${CC.RED}You don't own this!",
                                    "${CC.WHITE}Price: ${CC.GOLD}${it.price} ⛀",
                                    "${CC.WHITE}Can afford: ${
                                        if (it.canPurchase(player)) "${CC.GREEN}Yes" else "${CC.RED}No"
                                    }",
                                    "",
                                    "${CC.GREEN}Click to purchase!"
                                )
                                return@apply
                            }
                        }

                        addToLore(
                            "${CC.WHITE}Equipped: ${
                                if (equipped) "${CC.GREEN}Yes" else "${CC.RED}No"
                            }",
                            "",
                            if (equipped)
                                "${CC.RED}Click to un-equip!" else
                                    "${CC.GREEN}Click to equip!"
                        )

                        if (it is DynamicCosmetic<*>)
                        {
                            addToLore("${CC.YELLOW}Right-Click to customize!")
                        }
                    }
                    .toButton { _, type ->
                        if (it is PurchasableCosmetic)
                        {
                            if (!it.isOwnedBy(player))
                            {
                                if (!it.canPurchase(player))
                                {
                                    player.sendMessage(
                                        "${CC.RED}You cannot afford this cosmetic item!"
                                    )
                                    return@toButton
                                }

                                ConfirmMenu(
                                    title = "Purchase: ${it.name}",
                                    extraInfo = listOf(),
                                    confirm = true
                                ) { confirmed ->
                                    if (confirmed)
                                    {
                                        it.purchase(player)

                                        player.sendMessage(
                                            "${CC.GREEN}You have purchased the ${CC.WHITE}${it.name}${CC.GREEN} cosmetic item!"
                                        )
                                        player.playSound(
                                            player.location,
                                            Sound.NOTE_PLING,
                                            1.0f, 1.0f
                                        )
                                    }
                                }.openMenu(player)
                                return@toButton
                            }
                        }

                        if (it is DynamicCosmetic<*>)
                        {
                            if (type!!.isRightClick)
                            {
                                it.openEditor(player)
                                return@toButton
                            }
                        }

                        if (it.equipped(player))
                        {
                            it.unequipInternal(player)
                            player.sendMessage("${CC.RED}You un-equipped this cosmetic!")
                        } else
                        {
                            it.equipInternal(player)
                            player.sendMessage("${CC.GREEN}You equipped this cosmetic!")
                        }
                    }
            }

        return buttons
    }

    override fun onClose(player: Player, manualClose: Boolean)
    {
        if (manualClose)
        {
            Tasks.sync {
                CosmeticCategoryMenu().openMenu(player)
            }
        }
    }

    override fun getPrePaginatedTitle(player: Player) = "${category.name} Cosmetics"
}
