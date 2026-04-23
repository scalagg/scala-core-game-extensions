package gg.scala.game.extensions.cosmetics.utilities

import org.bukkit.EntityEffect
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 12/23/2023
 */
object DeathAnimatorUtilities
{
    fun animate(player: Player)
    {
        player.playEffect(EntityEffect.DEATH)
    }
}
