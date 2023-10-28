package gg.tropic.game.extensions.cosmetics.killeffects.cosmetics

import gg.tropic.game.extensions.cosmetics.CosmeticLike
import gg.tropic.game.extensions.cosmetics.PurchasableCosmetic
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/28/2023
 */
interface KillEffect :
    CosmeticLike,
    PurchasableCosmetic
{
    override fun unequip(player: Player)
    {
    }

    override fun equip(player: Player)
    {
    }

    fun applyTo(viewers: List<Player>, target: Player)
}
