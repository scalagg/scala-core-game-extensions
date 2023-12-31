package gg.tropic.game.extensions.customtags.commands

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.tropic.game.extensions.customtags.CustomChatTagService
import gg.tropic.game.extensions.customtags.menu.CustomTagsMenu
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

@AutoRegister
object CustomTagCommands : ScalaCommand()
{
    @CommandAlias("customtags")
    fun onCustomTagMenuOpen(player: Player): CompletableFuture<Void> =
        CustomChatTagService.getChatTagsFromPlayer(player.uniqueId).thenAccept {
            CustomTagsMenu(it).openMenu(player)
        }
}