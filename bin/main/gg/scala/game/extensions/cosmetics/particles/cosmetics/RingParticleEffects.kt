package gg.scala.game.extensions.cosmetics.particles.cosmetics

import gg.scala.game.extensions.cosmetics.CosmeticLike
import gg.scala.game.extensions.cosmetics.PurchasableCosmetic
import gg.scala.game.extensions.cosmetics.SessionScopeTickableCosmetic
import net.evilblock.cubed.util.nms.particle.ParticleMeta
import net.evilblock.cubed.util.nms.particle.ParticleUtil
import org.bukkit.entity.Player
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author GrowlyX
 * @since 10/27/2023
 */
interface RingParticleEffects :
    CosmeticLike,
    PurchasableCosmetic,
    SessionScopeTickableCosmetic<RingParticleEffectState>
{
    val particleType: Any

    override fun typeOf() = RingParticleEffectState::class
    override fun equip(player: Player)
    {

    }

    override fun unequip(player: Player)
    {

    }

    override fun tick(player: Player, state: RingParticleEffectState)
    {
        state.ticks += 1

        buildSmallRing(player, state.ticks, 0.5)
        buildBigRing(player, state.ticks, 0.9)
        buildSmallRing(player, state.ticks, 1.3)

        if (state.ticks >= 32) {
            state.ticks = 0
        }
    }

    private fun buildSmallRing(player: Player, ticks: Int, y: Double) {
        val angle = ticks * ((2 * Math.PI) / 32)
        val location = player.location.clone().add(0.6 * cos(angle), y, 0.6 * sin(angle))

        ParticleUtil.sendsParticleToAllRespectingVisibility(location.world, player,
            ParticleMeta(location, particleType, 0F, 0F, 0F, 0F, 2)
        )
    }

    private fun buildBigRing(player: Player, ticks: Int, y: Double) {
        val angle = ticks * ((2 * Math.PI) / 32)
        val location = player.location.clone().add(0.8 * cos(angle), y, 0.8 * sin(angle))

        ParticleUtil.sendsParticleToAllRespectingVisibility(location.world, player,
            ParticleMeta(location, particleType, 0F, 0F, 0F, 0F, 2)
        )
    }
}
