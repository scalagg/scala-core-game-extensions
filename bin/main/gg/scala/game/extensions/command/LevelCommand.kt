package gg.scala.game.extensions.command

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Optional
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.game.extensions.leveling.LevelSystemSyncService
import gg.scala.game.extensions.profile.event.LevelUpEvent
import gg.scala.game.extensions.profile.levelSymbol
import gg.scala.game.extensions.profile.profile
import net.evilblock.cubed.util.CC

/**
 * @author GrowlyX
 * @since 7/5/2023
 */
@AutoRegister
object LevelCommand : ScalaCommand()
{
    /**
     * Shows network level or a specific game level.
     */
    @CommandAlias("level")
    fun onLevel(player: ScalaPlayer, @Optional gamemode: String?)
    {
        if (gamemode != null)
        {
            val system = LevelSystemSyncService.byId(gamemode)
            if (system == null)
            {
                player.sendMessage("${CC.RED}Unknown level system '$gamemode'!")
                return
            }

            val levelData = player.profile.getGameLevelData(gamemode)
            val formatted = system.formatLevel(levelData.level)
            player.sendMessage(
                "${CC.SEC}${system.prettyName} Level: ${CC.PRI}${levelData.level} $formatted"
            )
            return
        }

        player.sendMessage(
            "${CC.SEC}Level: ${CC.PRI}${player.profile.level}$levelSymbol"
        )
    }

    @CommandAlias("simulate-level-up")
    @CommandPermission("op")
    fun onSimulateLevel(player: ScalaPlayer, @Optional gamemode: String?)
    {
        LevelUpEvent(
            player.profile, 1, 2,
            levelSystemId = gamemode
        ).callEvent()
    }
}
