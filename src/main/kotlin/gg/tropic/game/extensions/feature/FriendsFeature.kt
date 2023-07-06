package gg.tropic.game.extensions.feature

import gg.scala.commons.annotations.plugin.SoftDependency
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.flavor.service.ignore.IgnoreAutoScan
import gg.scala.friends.menu.FriendUIService
import gg.tropic.game.extensions.profile.coinSymbol
import gg.tropic.game.extensions.profile.levelSymbol
import gg.tropic.game.extensions.profile.profile
import gg.tropic.game.extensions.progress.ProgressBar
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Constants
import net.evilblock.cubed.util.math.Numbers
import net.evilblock.cubed.util.text.ProgressBarBuilder
import net.evilblock.cubed.util.text.TextUtil

/**
 * @author GrowlyX
 * @since 7/5/2023
 */
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
                "${CC.AQUA}Level progress:",
                "${CC.GRAY}[${
                    ProgressBar(
                        steps = 20,
                        char = Constants.THICK_VERTICAL_LINE[0],
                        completedColor = CC.GREEN,
                        uncompletedColor = CC.D_GRAY
                    ).build(
                        it.profile.getPercentageCompleteUntilNextLevel().toDouble()
                    )
                }${CC.GRAY}] ${CC.GREEN}${
                    "%.2f".format(
                        it.profile.getPercentageCompleteUntilNextLevel()
                    )
                }%",
                "",
                "${CC.GREEN}XP: ${CC.WHITE}${
                    Numbers.format(it.profile.experience)
                }",
                "",
                "${CC.AQUA}Next level: ${CC.WHITE}${it.profile.level + 1}",
                "${CC.AQUA}Next level XP: ${CC.WHITE}${
                    Numbers.format(it.profile.getXpRequiredForLevel(it.profile.level))
                }",
                "${CC.AQUA}XP needed: ${CC.WHITE}${
                    Numbers.format(it.profile.getXpRequiredToLevelUp())
                }"
            )
        }


    }
}
