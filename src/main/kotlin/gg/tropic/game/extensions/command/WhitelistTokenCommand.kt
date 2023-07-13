package gg.tropic.game.extensions.command

import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.acf.annotation.Subcommand
import gg.scala.commons.annotations.commands.AssignPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import gg.tropic.game.extensions.earlyaccess.WhitelistTokens
import net.evilblock.cubed.ScalaCommonsSpigot
import net.evilblock.cubed.util.CC
import org.bukkit.command.CommandSender

/**
 * @author GrowlyX
 * @since 7/12/2023
 */
@AutoRegister
@CommandAlias("whitelisttokens|wltoken|wltokens|wlts")
object WhitelistTokenCommand : ScalaCommand()
{
    @AssignPermission
    @CommandCompletion("@players")
    @Subcommand("give")
    fun onGive(sender: CommandSender, target: AsyncLemonPlayer, amount: Int) =
        target.validatePlayers(sender, true) {
            val tokens = WhitelistTokens
                .of(it.uniqueId)
                .join()
                ?: 0

            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hset(
                    "whitelist-tokens:tokens",
                    it.uniqueId.toString(),
                    "${tokens + amount}"
                )

            sender.sendMessage(
                "${CC.GREEN}Added $amount whitelist tokens to ${CC.AQUA}${it.name}'s${CC.GREEN} account."
            )
        }

    @Default
    fun onShow(player: ScalaPlayer) = WhitelistTokens
        .of(player.uniqueId)
        .thenAccept {
            it ?: throw ConditionFailedException(
                "You do not have any whitelist tokens!"
            )

            player.sendMessage(
                "${CC.GREEN}You have ${CC.WHITE}$it${CC.GREEN} whitelist tokens!",
                "${CC.GRAY}Use ${CC.AQUA}/wltoken <player>${CC.GRAY} to give another user one of your whitelist tokens!"
            )
        }

    @Default
    fun onGrant(player: ScalaPlayer, user: AsyncLemonPlayer) = user
        .validatePlayers(player.bukkit(), true) {
            val tokens = WhitelistTokens
                .of(player.uniqueId)
                .join()
                ?: throw ConditionFailedException(
                    "You do not have any whitelist tokens!"
                )

            if (tokens < 1)
            {
                throw ConditionFailedException(
                    "You do not have any whitelist tokens to grant!"
                )
            }

            val whitelisted = WhitelistTokens
                .isWhitelisted(it.uniqueId)
                .join()

            if (whitelisted)
            {
                throw ConditionFailedException(
                    "${CC.YELLOW}${it.name}${CC.RED} has already been granted a whitelist token!"
                )
            }

            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hset(
                    "whitelist-tokens:tokens",
                    player.uniqueId.toString(),
                    "${tokens - 1}"
                )

            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hset(
                    "whitelist-tokens:whitelisted",
                    it.uniqueId.toString(),
                    "true"
                )

            player.sendMessage(
                "${CC.GREEN}You have granted ${it.name} a whitelist token! They are now able to join the network."
            )
        }

}
