package gg.scala.game.extensions.cosmetics.utilities

import org.bukkit.EntityEffect
import org.bukkit.entity.Player

object DeathAnimatorUtilities
{
    fun animate(player: Player)
    {
        player.playEffect(EntityEffect.DEATH)
    }
}
