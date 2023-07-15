package gg.tropic.game.extensions.cosmetics

import gg.tropic.game.extensions.profile.profile
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/15/2023
 */
abstract class DynamicCosmeticConfigEditorMenu<T : DynamicCosmeticConfigStore>(
    private val category: CosmeticCategory,
    private val cosmetic: DynamicCosmetic<T>,
    private val configuration: T
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
        if (manualClose)
        {
            Tasks.sync {
                with(player.profile) {
                    cosmeticConfigs[cosmetic.resource.toString()] = configuration
                    save()
                }

                CosmeticBrowseMenu(category).openMenu(player)
            }
        }
    }

}
