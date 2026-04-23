package gg.scala.game.extensions.leveling.menu

import com.cryptomorin.xseries.XMaterial
import gg.scala.game.extensions.leveling.LevelSystem
import gg.scala.game.extensions.leveling.LevelSystemSyncService
import gg.scala.game.extensions.leveling.XpRange
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.entity.Player

/**
 * Paginated menu for managing XP ranges within a RANGE_BASED level system.
 *
 * @author Limo
 * @date 2/11/2026
 */
class ManageXpRangesMenu(private val system: LevelSystem) : PaginatedMenu()
{
    init
    {
        autoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player) = "Managing Xp Ranges"

    override fun getGlobalButtons(player: Player) = mapOf(
        4 to ItemBuilder
            .of(XMaterial.WRITABLE_BOOK)
            .name("${CC.GREEN}${CC.BOLD}Create XP Range")
            .addToLore(
                "${CC.GRAY}Click to create a new XP range."
            )
            .toButton { _, _ ->
                val range = XpRange()
                system.xpRanges.add(range)
                LevelSystemSyncService.update(system)

                player.sendMessage("${CC.GREEN}Created new XP range!")
                Button.playSuccess(player)
                EditXpRangeMenu(system, range).openMenu(player)
            }
    )

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        for ((index, range) in system.xpRanges.withIndex())
        {
            val lore = mutableListOf(
                "${CC.GRAY}Min Level: ${CC.WHITE}${range.minLevel}",
                "${CC.GRAY}Max Level: ${CC.WHITE}${range.maxLevel}",
                "${CC.GRAY}XP Required: ${CC.WHITE}${range.xpRequired}",
                "${CC.GRAY}Description: ${CC.WHITE}${range.description.ifEmpty { "None" }}",
                "",
                "${CC.YELLOW}Left-Click to edit!",
                "${CC.YELLOW}Right-Click to delete!"
            )

            buttons[buttons.size] = ItemBuilder
                .of(XMaterial.PAPER)
                .name("${CC.YELLOW}${CC.BOLD}XP Range ${index + 1}")
                .setLore(lore)
                .toButton { _, type ->
                    if (type!!.isRightClick)
                    {
                        system.xpRanges.remove(range)
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Deleted XP range ${index + 1}!")
                        Button.playSuccess(player)
                        ManageXpRangesMenu(system).openMenu(player)
                        return@toButton
                    }

                    Button.playNeutral(player)
                    EditXpRangeMenu(system, range).openMenu(player)
                }
        }

        return buttons
    }

    override fun onClose(player: Player, manualClose: Boolean)
    {
        if (manualClose)
        {
            Tasks.sync {
                ManageLevelSystemMenu(system).openMenu(player)
            }
        }
    }
}
