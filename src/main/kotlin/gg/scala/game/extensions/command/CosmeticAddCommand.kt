package gg.scala.game.extensions.command

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
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.profile.CorePlayerProfile
import gg.scala.game.extensions.profile.CorePlayerProfileService
import gg.scala.game.extensions.profile.profile
import net.evilblock.cubed.util.CC
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

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
        val policy = ImmutableResource(category, resource ?: "*")

        CorePlayerProfileService
            .useAsyncThenSaveGlobally(it.uniqueId) { account ->
                if (account.ownedCosmetics.contains(policy))
                {
                    throw ConditionFailedException(
                        "${CC.YELLOW}${it.name}'s${CC.RED} account already contains a similar resource policy."
                    )
                }

                account.ownedCosmetics.add(policy)
            }
            .thenRun {
                player.sendMessage(
                    arrayOf(
                        "${CC.GREEN}You've granted ${CC.YELLOW}${it.name}${CC.GREEN} the resource policy ${CC.WHITE}$policy${CC.GREEN}.",
                        "Policy Analysis: ${
                            if (policy.resource == "*")
                                "Allows access to all cosmetics in the ${policy.namespace} category"
                            else
                                "Allows access to the ${policy.resource} cosmetic in the ${policy.namespace} category"
                        }"
                    )
                )
            }
            .join()
    }
}
