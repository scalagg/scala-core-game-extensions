package gg.scala.game.extensions.cosmetics

import gg.scala.chattags.plugin.model.ChatTag
import gg.scala.game.extensions.cosmetics.proxied.ProxyCosmeticCategory
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.menus.ConfirmMenu
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.math.Numbers
import net.evilblock.cubed.util.time.TimeUtil
import org.bukkit.Sound
import org.bukkit.entity.Player
import kotlin.math.roundToInt

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
class CosmeticBrowseMenu(
    private val category: CosmeticCategory
) : PaginatedMenu()
{
    init
    {
        placeholdBorders = true
        updateAfterClick = true
        autoUpdate = true
    }

    override fun size(buttons: Map<Int, Button>) = 36
    override fun getMaxItemsPerPage(player: Player) = 14

    override fun getAllPagesButtonSlots() = (10..16) + (19..25)
    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        category.cosmetics()
            .forEach {
                buttons[buttons.size] = it.create()
                    .addToLore("")
                    .apply {
                        val equipped = it.equipped(player)
                        if (it is PurchasableCosmetic)
                        {
                            if (it.isOwnedBy(player))
                            {
                                addToLore(
                                    "${CC.GREEN}You own this!"
                                )
                            } else
                            {
                                if (!it.purchasable())
                                {
                                    addToLore(
                                        "${CC.RED}This cannot be purchased!"
                                    )
                                    return@apply
                                }

                                addToLore(
                                    "${CC.RED}You don't own this!",
                                    "${CC.WHITE}Price: ${
                                        if (it.discountPrice() != null)
                                        {
                                            "${CC.D_GRAY}⛀ ${CC.STRIKE_THROUGH}${
                                                Numbers.format(it.price.toLong())
                                            }${CC.R} ${CC.GOLD}⛀ ${
                                                Numbers.format(it.discountPrice()!!.toLong())
                                            } ${CC.B}(${it.discount()?.percentage}% off)"
                                        } else
                                        {
                                            "${CC.GOLD}⛀ ${
                                                Numbers.format(it.price.toLong())
                                            }"
                                        }
                                    }"
                                )

                                val discount = it.discount()
                                val expiration =  if (discount?.expirationDate != null)
                                {
                                    "${CC.WHITE}${TimeUtil.formatIntoAbbreviatedString(((discount.expirationDate!! - System.currentTimeMillis()) / 1000L).toInt())}"
                                } else
                                {
                                    null
                                }

                                if (expiration != null)
                                {
                                    addToLore(
                                        " ${CC.GRAY}($expiration${CC.GRAY} left!)",
                                        ""
                                    )
                                }

                                addToLore(
                                    "${CC.WHITE}Can afford: ${
                                        if (it.canPurchase(player)) "${CC.GREEN}Yes" else "${CC.RED}No"
                                    }",
                                    "",
                                    "${CC.GREEN}Click to purchase!"
                                )
                                return@apply
                            }
                        }

                        addToLore(
                            "${CC.WHITE}Equipped: ${
                                if (equipped) "${CC.GREEN}Yes" else "${CC.RED}No"
                            }",
                            "",
                            if (equipped)
                                "${CC.RED}Click to un-equip!" else
                                    "${CC.GREEN}Click to equip!"
                        )

                        if (it is DynamicCosmetic<*>)
                        {
                            addToLore("${CC.YELLOW}Right-Click to customize!")
                        }
                    }
                    .toButton { _, type ->
                        if (it is PurchasableCosmetic)
                        {
                            if (!it.isOwnedBy(player))
                            {
                                if (!it.purchasable())
                                {
                                    player.sendMessage("${CC.RED}This item cannot be purchased!")
                                    return@toButton
                                }

                                if (!it.canPurchase(player))
                                {
                                    Button.playFail(player)
                                    player.sendMessage(
                                        "${CC.RED}You cannot afford this cosmetic item!"
                                    )
                                    return@toButton
                                }

                                Button.playNeutral(player)

                                ConfirmMenu(
                                    title = "Purchase: ${it.name}",
                                    extraInfo = listOf(),
                                    confirm = true
                                ) { confirmed ->
                                    if (confirmed)
                                    {
                                        it.purchase(player)

                                        player.sendMessage(
                                            "${CC.GREEN}You have purchased the ${CC.WHITE}${it.name}${CC.GREEN} cosmetic item!"
                                        )
                                        player.playSound(
                                            player.location,
                                            Sound.NOTE_PLING,
                                            1.0f, 1.0f
                                        )

                                        Tasks.sync {
                                            openMenu(player)
                                        }
                                    }
                                }.openMenu(player)
                                return@toButton
                            }
                        }

                        if (it is DynamicCosmetic<*>)
                        {
                            if (type!!.isRightClick)
                            {
                                Button.playNeutral(player)
                                it.openEditor(player)
                                return@toButton
                            }
                        }

                        if (it.equipped(player))
                        {
                            Button.playNeutral(player)
                            it.unequipInternal(player)

                            player.sendMessage("${CC.RED}You un-equipped this cosmetic!")
                        } else
                        {
                            if (it !is MultiEquipCosmetic)
                            {
                                val equippedOnCosmetic = CosmeticRegistry
                                    .findRelatedTo(category)
                                    .firstOrNull { cosmetic ->
                                        cosmetic.equipped(player)
                                    }

                                if (equippedOnCosmetic != null && equippedOnCosmetic != it && equippedOnCosmetic is ProxyCosmetic)
                                {
                                    Button.playNeutral(player)
                                    equippedOnCosmetic.unequip(player)
                                    it.equipInternal(player)
                                    player.sendMessage("${CC.GREEN}You equipped this cosmetic!")
                                    return@toButton
                                }

                                if (equippedOnCosmetic != null)
                                {
                                    Button.playFail(player)
                                    player.sendMessage(
                                        "${CC.RED}You must un-equip the ${CC.BOLD}${equippedOnCosmetic.name}${CC.RED} cosmetic before equipping another one!"
                                    )
                                    return@toButton
                                }
                            }

                            Button.playNeutral(player)
                            it.equipInternal(player)
                            player.sendMessage("${CC.GREEN}You equipped this cosmetic!")
                        }
                    }
            }

        return buttons
    }

    override fun onClose(player: Player, manualClose: Boolean)
    {
        if (manualClose)
        {
            Tasks.sync {
                CosmeticCategoryMenu().openMenu(player)
            }
        }
    }

    override fun getPrePaginatedTitle(player: Player) = "Cosmetics: ${category.name}"
}
