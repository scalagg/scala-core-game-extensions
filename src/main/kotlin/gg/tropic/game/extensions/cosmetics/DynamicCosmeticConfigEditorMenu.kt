package gg.tropic.game.extensions.cosmetics

import gg.tropic.game.extensions.profile.profile
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.entity.Player

/**
 * Abstract menu class which saves the customized [DynamicCosmeticConfigStore] instance
 * of type T to the player's profile when the menu is closed by the player.
 *
 * @author GrowlyX
 * @since 7/15/2023
 */
abstract class DynamicCosmeticConfigEditorMenu<T : DynamicCosmeticConfigStore>(
    private val category: CosmeticCategory,
    private val cosmetic: DynamicCosmetic<T>,
    val configuration: T
) : PaginatedMenu()
{

    init
    {
        placeholdBorders = true
        updateAfterClick = true
    }

    abstract fun populate(
        player: Player,
        buttons: MutableMap<Int, Button>
    )

    override fun size(buttons: Map<Int, Button>) = 27
    override fun getMaxItemsPerPage(player: Player) = 7

    override fun getAllPagesButtonSlots() = (10..16).toList()

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()
        populate(player, buttons)
        // TODO: header item
        return buttons
    }

    override fun getPrePaginatedTitle(player: Player) = "Customizing ${cosmetic.name}..."

    override fun onClose(player: Player, manualClose: Boolean)
    {
        with(player.profile) {
            cosmeticConfigs[cosmetic.resource.toString()] = configuration
            save()
        }

        if (manualClose)
        {
            Tasks.sync {
                CosmeticBrowseMenu(category).openMenu(player)
            }
        }
    }

}
