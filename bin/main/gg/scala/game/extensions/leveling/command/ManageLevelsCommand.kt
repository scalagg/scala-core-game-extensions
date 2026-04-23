package gg.scala.game.extensions.leveling.command

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Optional
import gg.scala.commons.acf.annotation.Syntax
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.game.extensions.leveling.LevelSystem
import gg.scala.game.extensions.leveling.LevelSystemSyncService
import gg.scala.game.extensions.leveling.menu.ManageLevelSystemMenu
import gg.scala.game.extensions.leveling.menu.ManageLevelSystemsMenu
import net.evilblock.cubed.util.CC

/**
 * Admin commands for managing level systems.
 *
 * @author Limo
 * @date 2/11/2026
 */
@AutoRegister
object ManageLevelsCommand : ScalaCommand()
{
    @CommandAlias("manage-levels|managelevels")
    @CommandPermission("op")
    fun onManageLevels(player: ScalaPlayer)
    {
        ManageLevelSystemsMenu().openMenu(player.bukkit())
    }

    @CommandAlias("create-level-system|createlevelsystem")
    @CommandPermission("op")
    @Syntax("<id>")
    fun onCreateLevelSystem(player: ScalaPlayer, id: String)
    {
        val normalizedId = id.lowercase().replace(" ", "_")

        if (LevelSystemSyncService.byId(normalizedId) != null)
        {
            player.sendMessage("${CC.RED}A level system with ID '$normalizedId' already exists!")
            return
        }

        val system = LevelSystem(id = normalizedId, prettyName = id.replaceFirstChar { it.uppercase() })
        LevelSystemSyncService.create(system)

        player.sendMessage("${CC.GREEN}Created level system '$normalizedId'!")
        ManageLevelSystemMenu(system).openMenu(player.bukkit())
    }
}
