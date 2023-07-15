package gg.tropic.game.extensions.profile.booster

import com.cryptomorin.xseries.XMaterial
import gg.tropic.game.extensions.profile.profile
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.time.TimeUtil
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/13/2023
 */
class XpBoosterMenu : PaginatedMenu()
{
    init
    {
        placeholdBorders = true
        updateAfterClick = true
    }

    override fun getPrePaginatedTitle(player: Player) = "XP Boosters"
    override fun getAllPagesButtonSlots() = listOf(
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25
    )

    override fun getGlobalButtons(player: Player) =
        mutableMapOf(
            4 to ItemBuilder
                .of(XMaterial.CYAN_DYE)
                .name("${CC.D_AQUA}XP Boosters")
                .addToLore(
                    "${CC.WHITE}View all available network",
                    "${CC.WHITE}XP boosters below!",
                    "",
                    "${CC.GRAY}Your active booster:",
                    *player.profile.activeXpBooster
                        .let {
                            if (it == null)
                                arrayOf("${CC.RED}None")
                            else
                                arrayOf(
                                    "${CC.GRAY}Multiplier: ${CC.WHITE}${"%.2f".format(it.multiplier)}x",
                                    "${CC.GRAY}Expires: ${CC.WHITE}${
                                        TimeUtil.formatIntoAbbreviatedString(
                                            ((it.expiration - System.currentTimeMillis()) / 1000).toInt()
                                        )
                                    }"
                                )
                        }
                )
                .toButton()
        )

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        player.profile.availableXpBoosters
            .forEach {
                buttons[buttons.size] = ItemBuilder
                    .of(Material.PAPER)
                    .name("${CC.GREEN}${
                        "%.2f".format(it.multiplier)
                    }x Booster")
                    .addToLore(
                        "${CC.WHITE}Duration:",
                        "${CC.D_AQUA}${
                            TimeUtil.formatIntoAbbreviatedString(
                                (it.duration / 1000).toInt()
                            )
                        }",
                        "",
                        "${CC.GREEN}[Click to activate]"
                    )
                    .toButton { _, _ ->
                        if (player.profile.activeXpBooster != null)
                        {
                            player.sendMessage(
                                "${CC.RED}You already have an active booster! Please wait until it expires before activating a new one."
                            )
                            return@toButton
                        }

                        with(player.profile)
                        {
                            activateBooster(it)
                            save()

                            player.sendMessage(
                                "${CC.GREEN}You've activated a ${"%.2f".format(it.multiplier)}x booster for the next ${
                                    TimeUtil.formatIntoAbbreviatedString(
                                        (it.duration / 1000).toInt()
                                    )
                                }."
                            )

                            player.playSound(
                                player.location,
                                Sound.ORB_PICKUP,
                                1.0f, 1.0f
                            )
                        }
                    }
            }

        return buttons
    }

    override fun size(buttons: Map<Int, Button>) = 36
    override fun getMaxItemsPerPage(player: Player) = 14
}
