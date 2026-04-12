package gg.scala.game.extensions.leveling.menu

import com.cryptomorin.xseries.XMaterial
import gg.scala.game.extensions.leveling.LevelSystem
import gg.scala.game.extensions.leveling.LevelSystemSyncService
import gg.scala.game.extensions.leveling.XpRange
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.Menu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import net.evilblock.cubed.util.bukkit.prompt.NumberPrompt
import org.bukkit.entity.Player

/**
 * @author Limo
 * @date 2/11/2026
 */
class EditXpRangeMenu(
    private val system: LevelSystem,
    private val range: XpRange
) : Menu("Editing XP Range")
{
    init
    {
        autoUpdate = true
        placeholder = true
    }

    override fun size(buttons: Map<Int, Button>) = 27

    override fun getButtons(player: Player) = mapOf(
        10 to ItemBuilder
            .of(XMaterial.GREEN_WOOL)
            .name("${CC.YELLOW}${CC.BOLD}Min Level")
            .addToLore(
                "${CC.GRAY}Lowest level in this range.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${range.minLevel}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                NumberPrompt()
                    .withText("Enter the minimum level for this range")
                    .acceptInput { number ->
                        range.minLevel = number.toInt()
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Updated min level to ${number.toInt()}!")
                        Button.playSuccess(player)
                        EditXpRangeMenu(system, range).openMenu(player)
                    }
                    .start(player)
            },
        12 to ItemBuilder
            .of(XMaterial.RED_WOOL)
            .name("${CC.YELLOW}${CC.BOLD}Max Level")
            .addToLore(
                "${CC.GRAY}Highest level in this range.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${range.maxLevel}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                NumberPrompt()
                    .withText("Enter the maximum level for this range")
                    .acceptInput { number ->
                        range.maxLevel = number.toInt()
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Updated max level to ${number.toInt()}!")
                        Button.playSuccess(player)
                        EditXpRangeMenu(system, range).openMenu(player)
                    }
                    .start(player)
            },
        14 to ItemBuilder
            .of(XMaterial.EXPERIENCE_BOTTLE)
            .name("${CC.YELLOW}${CC.BOLD}XP Required")
            .addToLore(
                "${CC.GRAY}XP per level in this range.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${range.xpRequired}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                NumberPrompt()
                    .withText("Enter the XP required per level in this range")
                    .acceptInput { number ->
                        range.xpRequired = number.toLong()
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Updated XP required to ${number.toLong()}!")
                        Button.playSuccess(player)
                        EditXpRangeMenu(system, range).openMenu(player)
                    }
                    .start(player)
            },
        16 to ItemBuilder
            .of(XMaterial.NAME_TAG)
            .name("${CC.YELLOW}${CC.BOLD}Description")
            .addToLore(
                "${CC.GRAY}Label for this XP range.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${range.description.ifEmpty { "None" }}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter a description for this XP range")
                    .acceptInput { _, text ->
                        range.description = text
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Updated description to '$text'!")
                        Button.playSuccess(player)
                        EditXpRangeMenu(system, range).openMenu(player)
                    }
                    .start(player)
            }
    )

    override fun onClose(player: Player, manualClose: Boolean)
    {
        if (manualClose)
        {
            Tasks.sync {
                ManageXpRangesMenu(system).openMenu(player)
            }
        }
    }
}
