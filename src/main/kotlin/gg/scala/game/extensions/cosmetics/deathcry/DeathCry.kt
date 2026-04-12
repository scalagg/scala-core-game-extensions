package gg.scala.game.extensions.cosmetics.deathcry

import gg.scala.game.extensions.cosmetics.CosmeticLike
import gg.scala.game.extensions.cosmetics.MultiEquipCosmetic
import org.bukkit.entity.Player

/**
 * @author Limo
 * @date 2/10/2026
 */
interface DeathCry :
    CosmeticLike,
    MultiEquipCosmetic
{

    fun play(player: Player)
}
