package gg.scala.game.extensions.customtags.commands

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.game.extensions.customtags.CustomChatTagService
import gg.scala.game.extensions.customtags.menu.CustomTagsMenu
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

@AutoRegister
object CustomTagCommands : ScalaCommand()
{
    @CommandAlias("customtags")
    @CommandPermission("core.command.customtags")
    fun onCustomTagMenuOpen(player: Player) =
        CustomChatTagService
            .getChatTagsFromPlayer(player.uniqueId)
            .thenAccept {
                CustomTagsMenu(it).openMenu(player)
            }
}
