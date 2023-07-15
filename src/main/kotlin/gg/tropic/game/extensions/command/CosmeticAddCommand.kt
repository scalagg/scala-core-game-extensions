package gg.tropic.game.extensions.command

import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Optional
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.type.DataStoreStorageType
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.profile.CorePlayerProfile
import gg.tropic.game.extensions.profile.profile
import net.evilblock.cubed.util.CC
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

/**
 * @author GrowlyX
 * @since 7/13/2023
 */
@AutoRegister
object CosmeticAddCommand : ScalaCommand()
{
    @CommandAlias("add-cosmetic")
    @CommandCompletion("@players")
    @CommandPermission("core.command.addcosmetic")
    fun onAddCosmetic(
        player: CommandSender,
        target: AsyncLemonPlayer,
        category: String,
        @Optional resource: String?
    ) = target.validatePlayers(
        player, false
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

        val policy = ImmutableResource(category, resource ?: "*")
        if (account.ownedCosmetics.contains(policy))
        {
            throw ConditionFailedException(
                "${CC.YELLOW}${it.name}'s${CC.RED} account already contains a similar resource policy."
            )
        }

        account.ownedCosmetics.add(policy)
        account.save().join()

        player.sendMessage(arrayOf(
            "${CC.GREEN}You've granted ${CC.YELLOW}${it.name}${CC.GREEN} the resource policy ${CC.WHITE}$policy${CC.GREEN}.",
            "Policy Analysis: ${
                if (policy.resource == "*") 
                    "Allows access to all cosmetics in the ${policy.namespace} category"
                else
                    "Allows access to the ${policy.resource} cosmetic in the ${policy.namespace} category"
            }"
        ))
    }
}
