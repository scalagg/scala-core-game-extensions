package gg.tropic.game.extensions.command

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.tropic.game.extensions.profile.coinSymbol
import gg.tropic.game.extensions.profile.profile
import net.evilblock.cubed.util.CC

/**
 * @author GrowlyX
 * @since 7/5/2023
 */
@AutoRegister
object CoinsCommand : ScalaCommand()
{
    @CommandAlias("level")
    fun onLevel(player: ScalaPlayer)
    {
        player.sendMessage(
            "${CC.SEC}Coins: ${CC.GOLD}${player.profile.coins} $coinSymbol"
        )
    }
}
