package gg.scala.game.extensions.cosmetics.projectiletrails.cosmetics

import gg.scala.game.extensions.cosmetics.CosmeticLike
import gg.scala.game.extensions.cosmetics.NewlyReleased
import gg.scala.game.extensions.cosmetics.PurchasableCosmetic
import gg.scala.game.extensions.cosmetics.SessionScopeTickableCosmetic
import net.evilblock.cubed.util.nms.particle.ParticleMeta
import net.evilblock.cubed.util.nms.particle.ParticleUtil
import org.bukkit.entity.Player
import xyz.xenondevs.particle.ParticleEffect
import kotlin.math.cos
import kotlin.math.sin

interface ProjectileTrailCosmetic :
    CosmeticLike,
    PurchasableCosmetic
{
   val particleEffect: ParticleEffect

    override fun equip(player: Player)
    {

    }

    override fun unequip(player: Player)
    {

    }
}
