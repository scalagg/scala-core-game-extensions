package gg.scala.game.extensions.namemc.command

import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.annotation.*
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.game.extensions.namemc.NameMCConfig
import gg.scala.game.extensions.namemc.NameMCDataSync
import gg.scala.game.extensions.namemc.NameMCService
import gg.scala.game.extensions.namemc.NameMCVoteRequest
import gg.scala.game.extensions.profile.CorePlayerProfileService
import gg.scala.lemon.metadata.NetworkMetadataDataSync
import gg.scala.lemon.player.LemonPlayer
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Constants
import org.bukkit.Bukkit
import org.bukkit.ChatColor

/**
 * @author Topu
 * @date 2/7/2025
 */
@AutoRegister
@CommandAlias("namemc|nmc")
object NameMCCommand : ScalaCommand()
{

    @Default
    @HelpCommand
    fun onHelp(player: ScalaPlayer)
    {
        player.sendMessage("""
        ${CC.GRAY}--------------------------------------------------
        ${CC.B_PRI}NameMC Voting Rewards
        ${CC.YELLOW}Want to earn exclusive rewards? Follow these steps:
        
        ${CC.WHITE}1. Visit: ${CC.GREEN}${NameMCDataSync.cached().targetUrl}
        ${CC.WHITE}2. Click on the ${CC.GREEN}"❤ Like" ${CC.WHITE}button (You need to be logged in).
        ${CC.WHITE}3. Use ${CC.PRI}/namemc verify ${CC.WHITE}to claim your reward!
        
        ${CC.YELLOW}Thank you for supporting our server! ${CC.RED}❤
        ${CC.GRAY}--------------------------------------------------
    """.trimIndent())
    }

    @Subcommand("verify")
    fun onVerify(player: ScalaPlayer)
    {
        val profile = CorePlayerProfileService.find(player.uniqueId)

        if (profile?.hasVotedOnNameMC != null) {
            player.sendMessage("${CC.RED}You have already claimed your reward!")
            return
        }

        NameMCService.submit(NameMCVoteRequest(player.uniqueId) { hasVoted ->
            if (hasVoted) {
                val cached = NameMCDataSync.cached()

                cached.rewardCommand.forEach { command ->
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.format(player.bukkit().name))
                }

                profile?.hasVotedOnNameMC = System.currentTimeMillis()

                player.sendMessage("${CC.B_PRI}${NetworkMetadataDataSync.serverName()}${CC.GRAY}${Constants.DOUBLE_ARROW_RIGHT} ${CC.GREEN}✔ Thank you for liking our server! You have been rewarded.")
            } else {
                player.sendMessage("${CC.B_PRI}${NetworkMetadataDataSync.serverName()}${CC.GRAY}${Constants.DOUBLE_ARROW_RIGHT} ${CC.RED}✖ You haven't liked our server yet! ${CC.YELLOW}Like us on NameMC to claim your reward.")
            }
        })
    }
}