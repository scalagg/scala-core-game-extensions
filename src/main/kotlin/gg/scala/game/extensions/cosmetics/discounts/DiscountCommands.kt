package gg.scala.game.extensions.cosmetics.discounts

import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.acf.annotation.Description
import gg.scala.commons.acf.annotation.HelpCommand
import gg.scala.commons.acf.annotation.Subcommand
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.game.extensions.cosmetics.discounts.menus.DiscountManageMenu

/**
 * @author GrowlyX
 * @since 8/11/2024
 */
@AutoRegister
@CommandAlias("discounts|discount")
@CommandPermission("core.command.discounts")
object DiscountCommands : ScalaCommand()
{
    @Default
    @HelpCommand
    fun default(help: CommandHelp)
    {
        help.showHelp()
    }

    @Subcommand("manage")
    @Description("Manage all discounts.")
    fun manage(player: ScalaPlayer)
    {
        DiscountManageMenu().openMenu(player.bukkit())
    }
}
