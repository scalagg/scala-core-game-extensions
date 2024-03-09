package gg.tropic.game.extensions.command

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.player.LemonPlayer
import gg.tropic.game.extensions.profile.booster.XpBoosterMenu
import gg.tropic.game.extensions.profile.coinSymbol
import gg.tropic.game.extensions.profile.profile
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.math.Numbers

/**
 * @author GrowlyX
 * @since 7/5/2023
 */
@AutoRegister
object CoinsCommand : ScalaCommand()
{
    @CommandAlias("coins")
    fun onLevel(player: ScalaPlayer)
    {
        player.sendMessage(
            "${CC.GOLD}Coins: ${CC.BOLD}${CC.GOLD}${
                Numbers.format(player.profile.coins)
            } $coinSymbol"
        )
    }

    @CommandAlias("boosters|xpboosters")
    fun onXpBoosters(player: ScalaPlayer) =
        XpBoosterMenu().openMenu(player.bukkit())

    @CommandAlias("add-coins")
    @CommandCompletion("@players")
    @CommandPermission("op")
    fun onAddCoins(player: ScalaPlayer, target: LemonPlayer, amount: Int)
    {
        target.bukkitPlayer!!.profile.addCoins(amount, feedback = player::sendMessage, reason = "console")
    }

}
