package gg.tropic.game.extensions.feature

import gg.scala.commons.annotations.plugin.SoftDependency
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.flavor.service.ignore.IgnoreAutoScan
import gg.scala.friends.menu.FriendUIService
import gg.tropic.game.extensions.profile.coinSymbol
import gg.tropic.game.extensions.profile.profile
import net.evilblock.cubed.util.CC

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
                "${CC.GRAY}Level: ${CC.GREEN}${it.profile.level}",
                "${CC.GOLD}Coins: ${CC.GOLD}${it.profile.coins} $coinSymbol",
                "",
                "${CC.WHITE}Level progress:",
                "${CC.GRAY}Complete: ${CC.AQUA}${
                    "%.2f".format(it.profile.getPercentageCompleteUntilNextLevel())
                }"
            )
        }
    }
}
