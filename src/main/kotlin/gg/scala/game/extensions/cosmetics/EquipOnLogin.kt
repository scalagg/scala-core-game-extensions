package gg.scala.game.extensions.cosmetics

import org.bukkit.entity.Player

interface EquipOnLogin
{
    fun shouldEquipOnLogin(player: Player): Boolean
    {
        return true
    }
}
