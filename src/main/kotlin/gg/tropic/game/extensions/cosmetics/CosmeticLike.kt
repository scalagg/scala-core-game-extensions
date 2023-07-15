package gg.tropic.game.extensions.cosmetics

import gg.tropic.game.extensions.profile.profile
import org.bukkit.entity.Player

/**
 * Resembles cosmetic items.
 *
 * @author GrowlyX
 * @since 7/14/2023
 */
interface CosmeticLike : ResourceHolder, FrontendComponent
{
    fun unequip(player: Player)
    fun equip(player: Player)

    fun canEquip(player: Player) = player.profile
        .run {
            !equippedCosmetics.contains(resource)
        }

    fun equipped(player: Player) = player.profile
        .equippedCosmetics.contains(resource)

    fun setEquipped(player: Player, value: Boolean)
    {
        if (value)
        {
            player.profile.equippedCosmetics.add(resource)
        } else
        {
            player.profile.equippedCosmetics.remove(resource)
        }
    }
}
