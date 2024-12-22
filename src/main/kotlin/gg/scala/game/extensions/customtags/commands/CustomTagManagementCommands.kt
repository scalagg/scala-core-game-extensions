package gg.scala.game.extensions.customtags.commands

import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import gg.scala.lemon.util.QuickAccess
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.type.DataStoreStorageType
import gg.scala.game.extensions.customtags.CustomChatTagService
import gg.scala.game.extensions.customtags.menu.PendingCustomTagMenu
import gg.scala.game.extensions.profile.CorePlayerProfile
import gg.scala.game.extensions.profile.CorePlayerProfileService
import gg.scala.game.extensions.profile.profile
import net.evilblock.cubed.util.CC
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@AutoRegister
object CustomTagManagementCommands : ScalaCommand()
{
    @CommandAlias("add-customtagtoken")
    @CommandCompletion("@players")
    @CommandPermission("core.command.addcustomtagtoken")
    fun onAddCustomTagToken(
        sender: CommandSender,
        target: AsyncLemonPlayer,
        amount: Int
    ) = target.validatePlayers(
        sender, false
    ) {
        CorePlayerProfileService
            .useAsyncThenSaveGlobally(it.uniqueId) { account ->
                if (account.customTagTokens == null)
                {
                    account.customTagTokens = 0
                }

                account.customTagTokens = account.customTagTokens!! + amount
            }
            .thenRun {
                sender.sendMessage(
                    "${
                        QuickAccess.computeColoredName(it.uniqueId, it.name).join()
                    } ${CC.GREEN}has been issued ${CC.YELLOW}$amount ${CC.GREEN}custom tag tokens!"
                )
            }
            .join()
    }

    @CommandAlias("managecustomtags")
    @CommandPermission("core.command.managecustomtags")
    fun onManageCustomTags(sender: Player) = CustomChatTagService
        .getAllPendingChatTags()
        .thenAccept {
            PendingCustomTagMenu(it).openMenu(sender)
        }
}

