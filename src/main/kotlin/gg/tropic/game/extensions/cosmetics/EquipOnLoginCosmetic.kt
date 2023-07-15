package gg.tropic.game.extensions.cosmetics

import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
interface EquipOnLoginCosmetic
{
    fun shouldEquipOnLogin(player: Player): Boolean
    {
        return true
    }
}
