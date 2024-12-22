package gg.scala.game.extensions.cosmetics

import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
interface EquipOnLogin
{
    fun shouldEquipOnLogin(player: Player): Boolean
    {
        return true
    }
}
