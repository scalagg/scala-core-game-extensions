package gg.tropic.game.extensions.cosmetics.messagebundles.cosmetics

import gg.tropic.game.extensions.cosmetics.CosmeticLike
import gg.tropic.game.extensions.cosmetics.MultiEquipCosmetic
import gg.tropic.game.extensions.cosmetics.PurchasableCosmetic
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/26/2023
 */
interface MessageBundle : CosmeticLike, PurchasableCosmetic, MultiEquipCosmetic
{
    val phrases: List<String>

    override fun equip(player: Player)
    {

    }

    override fun unequip(player: Player)
    {

    }

    override val price: Float
        get() = 1500.0f

    override val icon: ItemBuilder
        get() = ItemBuilder
            .of(Material.BOOK)

    override val description: List<String>
        get()
        {
            return phrases
                .map {
                    "${CC.GRAY}...was ${CC.WHITE}$it${CC.GRAY} by..."
                } + listOf(
                    "",
                    "${CC.WHITE}Selects a random phrase from",
                    "${CC.WHITE}the ones listed above!"
                )
        }
}
