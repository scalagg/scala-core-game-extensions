package gg.scala.game.extensions.leveling.menu

import com.cryptomorin.xseries.XMaterial
import gg.scala.game.extensions.leveling.LevelSystem
import gg.scala.game.extensions.leveling.LevelSystemSyncService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import org.bukkit.entity.Player

/**
 * @author Limo
 * @date 2/11/2026
 */
class ManageLevelSystemsMenu : PaginatedMenu()
{
    init
    {
        autoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player) = "Managing Level Systems"

    override fun getGlobalButtons(player: Player) = mapOf(
        4 to ItemBuilder
            .of(XMaterial.WRITABLE_BOOK)
            .name("${CC.GREEN}${CC.BOLD}Create Level System")
            .addToLore(
                "${CC.GRAY}Click to create a new level system."
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter a unique ID for the level system ${CC.GRAY}(e.g. bedwars, skywars)")
                    .acceptInput { _, text ->
                        val id = text.lowercase().replace(" ", "_")

                        if (LevelSystemSyncService.byId(id) != null)
                        {
                            player.sendMessage("${CC.RED}A level system with ID '$id' already exists!")
                            Button.playFail(player)
                            ManageLevelSystemsMenu().openMenu(player)
                            return@acceptInput
                        }

                        val system = LevelSystem(id = id, prettyName = text.replaceFirstChar { it.uppercase() })
                        LevelSystemSyncService.create(system)

                        player.sendMessage("${CC.GREEN}Created level system '$id'!")
                        Button.playSuccess(player)
                        ManageLevelSystemMenu(system).openMenu(player)
                    }
                    .start(player)
            }
    )

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        for ((_, system) in LevelSystemSyncService.all())
        {
            val lore = mutableListOf<String>()
            lore.addAll(system.getSummaryLore())

            buttons[buttons.size] = ItemBuilder
                .of(XMaterial.EXPERIENCE_BOTTLE)
                .name("${CC.YELLOW}${CC.BOLD}${system.prettyName}")
                .setLore(lore)
                .toButton { _, type ->
                    if (type!!.isRightClick)
                    {
                        LevelSystemSyncService.delete(system.id)
                        player.sendMessage("${CC.GREEN}Deleted level system '${system.id}'!")
                        Button.playSuccess(player)
                        ManageLevelSystemsMenu().openMenu(player)
                        return@toButton
                    }

                    Button.playNeutral(player)
                    ManageLevelSystemMenu(system).openMenu(player)
                }
        }

        return buttons
    }
}
