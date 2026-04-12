package gg.scala.game.extensions.feature

import gg.scala.commons.annotations.plugin.SoftDependency
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.flavor.service.ignore.IgnoreAutoScan
import gg.scala.friends.menu.FriendUIService
import gg.scala.lemon.channel.channels.DefaultChatChannel
import gg.scala.game.extensions.CoreGameExtensions
import gg.scala.game.extensions.profile.CorePlayerProfileService
import gg.scala.game.extensions.profile.coinSymbol
import gg.scala.game.extensions.profile.levelSymbol
import gg.scala.game.extensions.profile.profile
import gg.scala.game.extensions.progress.ProgressBar
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Constants
import net.evilblock.cubed.util.math.Numbers

import net.evilblock.cubed.util.text.TextUtil
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

@Service
@IgnoreAutoScan
@SoftDependency("Friends")
object FriendsFeature
{
    @Configure
    fun configure()
    {

        FriendUIService.initialLorePipe = {
            listOf(
                "${CC.GRAY}Level: ${CC.GREEN}${it.profile.level} $levelSymbol",
                "${CC.GRAY}Coins: ${CC.GOLD}${
                    Numbers.format(it.profile.coins)
                } $coinSymbol",
                "",
                "${CC.B_GREEN}Level progress:",
                "${CC.GRAY}[${
                    ProgressBar(
                        steps = 26,
                        char = Constants.THICK_VERTICAL_LINE[0],
                        completedColor = CC.GREEN,
                        uncompletedColor = CC.D_GRAY
                    ).build(
                        it.profile.getPercentageCompleteUntilNextLevel().toDouble()
                    )
                }${CC.GRAY}] ${CC.WHITE}${
                    "%.2f".format(
                        it.profile.getPercentageCompleteUntilNextLevel()
                    )
                }%",
                "",
                "${CC.GREEN}XP: ${CC.WHITE}${
                    Numbers.format(it.profile.experience)
                }",
                "",
                "${CC.GREEN}Next level: ${CC.WHITE}${it.profile.level + 1}",
                "${CC.GREEN}XP required: ${CC.WHITE}${
                    Numbers.format(
                        it.profile.getXpRequiredToAdvanceToLevel(it.profile.level + 1)
                    )
                }"
            )
        }
    }
}
