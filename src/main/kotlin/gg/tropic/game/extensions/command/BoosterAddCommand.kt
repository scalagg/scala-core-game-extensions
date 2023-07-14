package gg.tropic.game.extensions.command

import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.type.DataStoreStorageType
import gg.tropic.game.extensions.profile.CorePlayerProfile
import gg.tropic.game.extensions.profile.booster.XpBooster
import gg.tropic.game.extensions.profile.profile
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.time.Duration
import net.evilblock.cubed.util.time.TimeUtil
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

/**
 * @author GrowlyX
 * @since 7/13/2023
 */
@AutoRegister
object BoosterAddCommand : ScalaCommand()
{
    @CommandAlias("add-booster")
    @CommandPermission("core.command.addbooster")
    fun onAddBooster(
        player: CommandSender,
        target: AsyncLemonPlayer,
        multiplier: Float,
        duration: Duration
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

        account.availableXpBoosters.add(
            XpBooster(
                multiplier = multiplier,
                duration = duration.get()
            )
        )
        account.save().join()

        player.sendMessage(
            "${CC.GREEN}You've granted ${CC.YELLOW}${it.name}${CC.GREEN} a ${
                "%.2f".format(multiplier)
            }x booster for the next ${
                TimeUtil.formatIntoAbbreviatedString((duration.get() / 1000).toInt())
            }."
        )
    }
}
