package gg.scala.game.extensions.economy.command

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.game.extensions.economy.menu.ManageEconomiesMenu

/**
 * Admin commands for managing economy/currency types.
 *
 * @author Limo
 * @date 2/11/2026
 */
@AutoRegister
object ManageEconomiesCommand : ScalaCommand()
{
    @CommandAlias("manage-economies|manageeconomies")
    @CommandPermission("op")
    fun onManageEconomies(player: ScalaPlayer)
    {
        ManageEconomiesMenu().openMenu(player.bukkit())
    }
}
