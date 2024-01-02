package gg.tropic.game.extensions.customtags.commands

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
import gg.tropic.game.extensions.customtags.CustomChatTagService
import gg.tropic.game.extensions.customtags.menu.CustomTagManagementMenu
import gg.tropic.game.extensions.profile.CorePlayerProfile
import gg.tropic.game.extensions.profile.profile
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
        val account = Bukkit.getPlayer(it.uniqueId)
            ?.profile
            ?: DataStoreObjectControllerCache
                .findNotNull<CorePlayerProfile>()
                .load(
                    it.uniqueId,
                    DataStoreStorageType.MONGO
                )
                .join()
            ?: throw ConditionFailedException(
                "${CC.YELLOW}${it.name}${CC.RED} does not have a core profile."
            )

        if (account.customTagTokens == null)
        {
            account.customTagTokens = 0
        }

        account.customTagTokens = account.customTagTokens!! + amount
        account.save().join()

        sender.sendMessage(
            "${
                QuickAccess.computeColoredName(it.uniqueId, it.name).join()
            } ${CC.GREEN}has been issued ${CC.YELLOW}$amount ${CC.GREEN}custom tag tokens!"
        )
    }

    @CommandAlias("managecustomtags")
    @CommandPermission("core.command.managecustomtags")
    fun onManageCustomTags(sender: Player) = CustomChatTagService.retrieveAllChatTags()
        .thenAccept {
            if (it.isEmpty())
            {
                throw ConditionFailedException(
                    "There are currently no pending tags to accept!"
                )
            } else
            {
                CustomTagManagementMenu(it).openMenu(sender)
            }
        }
}

