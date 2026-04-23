package gg.scala.game.extensions.command

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.Optional
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.game.extensions.profile.menu.ProfileMenu
import gg.scala.friends.menu.OtherPlayerProfileMenu
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import java.util.concurrent.CompletableFuture

/**
 * @author Limo
 * @date 2/12/2026
 */
@AutoRegister
object ProfileCommand : ScalaCommand()
{
    @CommandCompletion("@players")
    @CommandAlias("profile|myprofile|profilemenu")
    fun onProfile(
        player: ScalaPlayer, @Optional target: AsyncLemonPlayer?
    ): CompletableFuture<Void>
    {
        if (target == null)
        {
            ProfileMenu().openMenu(player.bukkit())
            return CompletableFuture
                .completedFuture(null)
        }

        return target
            .validatePlayers(
                player.bukkit(), false
            ) {
                OtherPlayerProfileMenu(it.uniqueId)
                    .openMenu(player.bukkit())
            }
    }
}
