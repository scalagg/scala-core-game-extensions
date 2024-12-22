package gg.scala.game.extensions.command

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
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
    @CommandAlias("level")
    fun onLevel(player: ScalaPlayer)
    {
        player.sendMessage(
            "${CC.GREEN}Level: ${CC.WHITE}${player.profile.level} $levelSymbol"
        )
    }

    @CommandAlias("simulate-level-up")
    @CommandPermission("op")
    fun onSimulateLevel(player: ScalaPlayer)
    {
        LevelUpEvent(
            player.profile, 1, 2
        ).callEvent()
    }
}
