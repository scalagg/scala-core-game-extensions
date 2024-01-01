package gg.tropic.game.extensions.cosmetics

import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
class CosmeticCategoryMenu : PaginatedMenu()
{
    init
    {
        placeholdBorders = true
    }

    override fun size(buttons: Map<Int, Button>) = 27
    override fun getMaxItemsPerPage(player: Player) = 7

    override fun getAllPagesButtonSlots() = (10..16).toList()
    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        CosmeticRegistry.categories()
            .forEach {
                val owned = it.ownedCosmetics(player).size
                val allCosmeticsCount = it.cosmetics().size

                val percentageCollected = if (allCosmeticsCount == 0)
                    0.0f else (owned / allCosmeticsCount) * 100.0f

                buttons[buttons.size] = it.create()
                    .addToLore(
                        "",
                        "${CC.WHITE}Collected: ${CC.PRI}$owned/$allCosmeticsCount ${CC.GRAY}(${
                            "%.2f".format(percentageCollected)
                        }%)",
                        "",
                        "${CC.GREEN}Click to browse!"
                    )
                    .toButton { _, _ ->
                        CosmeticBrowseMenu(it).openMenu(player)
                        Button.playNeutral(player)
                    }
            }

        return buttons
    }

    override fun getPrePaginatedTitle(player: Player) = "Cosmetics"
}
