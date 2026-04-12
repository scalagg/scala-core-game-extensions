package gg.scala.game.extensions.command

import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.type.DataStoreStorageType
import gg.scala.game.extensions.profile.CorePlayerProfile
import gg.scala.game.extensions.profile.CorePlayerProfileService
import gg.scala.game.extensions.profile.booster.XpBooster
import gg.scala.game.extensions.profile.profile
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.time.Duration
import net.evilblock.cubed.util.time.TimeUtil
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

@AutoRegister
object BoosterAddCommand : ScalaCommand()
{
    @CommandAlias("add-booster")
    @CommandCompletion("@players")
    @CommandPermission("core.command.addbooster")
    fun onAddBooster(
        player: CommandSender,
        target: AsyncLemonPlayer,
        multiplier: Float,
        duration: Duration
    ) = target.validatePlayers(
        player, false
    ) {
        CorePlayerProfileService
            .useAsyncThenSaveGlobally(it.uniqueId) { account ->
                account.availableXpBoosters.add(
                    XpBooster(
                        multiplier = multiplier,
                        duration = duration.get()
                    )
                )
            }
            .thenRun {
                player.sendMessage(
                    "${CC.GREEN}You've granted ${CC.YELLOW}${it.name}${CC.GREEN} a ${
                        "%.2f".format(multiplier)
                    }x booster for the next ${
                        TimeUtil.formatIntoAbbreviatedString((duration.get() / 1000).toInt())
                    }."
                )
            }
            .join()
    }
}
