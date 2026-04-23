package gg.scala.game.extensions.namemc.command.admin

import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.annotation.*
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.game.extensions.namemc.NameMCDataSync
import gg.scala.game.extensions.namemc.NameMCService
import gg.scala.game.extensions.namemc.NameMCVoteRequest
import gg.scala.lemon.player.LemonPlayer
import net.evilblock.cubed.menu.menus.TextEditorMenu
import net.evilblock.cubed.util.CC
import org.bukkit.entity.Player

/**
 * @author Topu
 * @date 2/7/2025
 */
@AutoRegister
@CommandAlias("namemc-admin|nmc-admin|namemcadmin")
@CommandPermission("namemc.admin")
object NameMCAdminCommands : ScalaCommand()
{

    @Default
    @HelpCommand
    fun onHelp(help: CommandHelp)
    {
        help.showHelp()
    }

    @Subcommand("rewards")
    fun onRewards(player: ScalaPlayer) = with(NameMCDataSync.cached()) {
        with(
            object : TextEditorMenu(rewardCommand)
            {
                override fun getPrePaginatedTitle(player: Player) =
                    "Editing: NameMC Rewards"

                override fun onClose(player: Player)
                {

                }

                override fun onSave(player: Player, list: List<String>)
                {
                    rewardCommand.clear()
                    rewardCommand.addAll(list)

                    NameMCDataSync.sync(this@with)
                    player.sendMessage("${CC.GREEN}Updated namemc rewards!")
                }
            }
        ) {
            openMenu(player.bukkit())
        }
    }

    @Subcommand("toggle")
    fun onToggle(player: ScalaPlayer)
    {
        val cached = NameMCDataSync.cached()
        cached.enabled = !cached.enabled
        player.sendMessage("${CC.YELLOW}NameMC rewards are now ${if (cached.enabled) "${CC.GREEN}enabled" else "${CC.RED}disabled"}${CC.YELLOW}.")
    }

    @Subcommand("check")
    fun onCheck(player: ScalaPlayer, target: LemonPlayer)
    {
        NameMCService.submit(NameMCVoteRequest(player.uniqueId) { hasVoted ->
            player.sendMessage(
                if (hasVoted) "${CC.GREEN}${target.name} has liked the server!"
                else "${CC.RED}${target.name} has not liked the server."
            )
        })
    }
}