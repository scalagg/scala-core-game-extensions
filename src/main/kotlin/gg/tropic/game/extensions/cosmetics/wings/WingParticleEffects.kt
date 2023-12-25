package gg.tropic.game.extensions.cosmetics.wings

import gg.tropic.game.extensions.cosmetics.CosmeticLike
import gg.tropic.game.extensions.cosmetics.PurchasableCosmetic
import gg.tropic.game.extensions.cosmetics.SessionScopeTickableCosmetic
import net.evilblock.cubed.util.nms.MinecraftReflection
import net.evilblock.cubed.util.nms.particle.ParticleMeta
import net.evilblock.cubed.util.nms.particle.ParticleUtil
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/27/2023
 */
interface WingParticleEffects :
    CosmeticLike,
    PurchasableCosmetic,
    SessionScopeTickableCosmetic<WingParticleEffectState>
{
    val primary: Color
    val secondary: Color
    val tertiary: Color

    override fun typeOf() = WingParticleEffectState::class
    override fun equip(player: Player)
    {

    }

    override fun unequip(player: Player)
    {

    }

    override fun tick(player: Player, state: WingParticleEffectState)
    {
        if (System.currentTimeMillis() - state.lastMovement < 500L)
        {
            return
        }

        ParticleUtil.sendsParticleToAllRespectingVisibility(
            player.world, player,
            *generateParticles(player).toTypedArray()
        )
    }

    private fun generateParticles(player: Player): List<ParticleMeta>
    {
        return arrayListOf<ParticleMeta>().also { particles ->
            val ws = 75

            val wp = if (player.isFlying)
            {
                ws
            } else
            {
                75
            }

            var loc = player.location.clone()
            loc.pitch = 0.0f
            loc.add(0.0, 0.0, 0.0)
            loc.add(loc.direction.multiply(-0.2))
            loc.yaw = loc.yaw + wp

            var loc2 = loc.clone()

            particles.addAll(render(loc, loc2))

            loc = player.location.clone()
            loc.pitch = 0.0f
            loc.add(0.0, 0.0, 0.0)
            loc.add(loc.direction.multiply(-0.2))
            loc.yaw = loc.yaw - wp

            loc2 = loc.clone()

            particles.addAll(render(loc, loc2))
        }
    }

    private fun render(loc: Location, loc2: Location): List<ParticleMeta>
    {
        return arrayListOf<ParticleMeta>().also { particles ->
            var loc2 = loc2

            val particle = MinecraftReflection.getEnumParticle("REDSTONE")!!

            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.2000000000000002)),
                    particle,
                    primary,
                    0.0F,
                    0
                )
            )
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    primary,
                    0.0F,
                    0
                )
            )

            loc.add(0.0, 0.2, 0.0)
            loc2 = loc.clone()

            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.0)), particle, primary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.2000000000000002)),
                    particle,
                    primary,
                    0.0F,
                    0
                )
            )
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    primary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.6)), particle, primary, 0.0F, 0))

            loc.add(0.0, 0.2, 0.0)
            loc2 = loc.clone()

            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.8)), particle, primary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.0)), particle, primary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.2000000000000002)),
                    particle,
                    primary,
                    0.0F,
                    0
                )
            )
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    primary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.6)), particle, primary, 0.0F, 0))

            loc.add(0.0, 0.2, 0.0)
            loc2 = loc.clone()

            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-0.6000000000000001)),
                    particle,
                    primary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.8)), particle, primary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.0)), particle, primary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.2000000000000002)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.6)), particle, secondary, 0.0F, 0))

            loc.add(0.0, 0.2, 0.0)
            loc2 = loc.clone()

            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.4)), particle, primary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-0.6000000000000001)),
                    particle,
                    primary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.8)), particle, primary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.0)), particle, secondary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.2000000000000002)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )

            loc.add(0.0, 0.2, 0.0)
            loc2 = loc.clone()

            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.4)), particle, primary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-0.6000000000000001)),
                    particle,
                    primary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.8)), particle, secondary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.0)), particle, secondary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.2000000000000002)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )

            loc.add(0.0, 0.2, 0.0)
            loc2 = loc.clone()

            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.4)), particle, primary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-0.6000000000000001)),
                    particle,
                    primary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.8)), particle, secondary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.0)), particle, secondary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.2000000000000002)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.6)), particle, secondary, 0.0F, 0))

            loc.add(0.0, 0.2, 0.0)
            loc2 = loc.clone()

            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.4)), particle, primary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-0.6000000000000001)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.8)), particle, secondary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.0)), particle, secondary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.2000000000000002)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    tertiary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.6)), particle, tertiary, 0.0F, 0))

            loc.add(0.0, 0.2, 0.0)
            loc2 = loc.clone()

            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.4)), particle, secondary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-0.6000000000000001)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.8)), particle, secondary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.0)), particle, secondary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.2000000000000002)),
                    particle,
                    tertiary,
                    0.0F,
                    0
                )
            )
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    tertiary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.6)), particle, tertiary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.8)), particle, tertiary, 0.0F, 0))

            loc.add(0.0, 0.2, 0.0)
            loc2 = loc.clone()

            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-2.0)), particle, tertiary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-0.6000000000000001)),
                    particle,
                    secondary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-0.8)), particle, secondary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.0)), particle, tertiary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.2000000000000002)),
                    particle,
                    tertiary,
                    0.0F,
                    0
                )
            )
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    tertiary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.6)), particle, tertiary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.8)), particle, tertiary, 0.0F, 0))

            loc.add(0.0, 0.2, 0.0)
            loc2 = loc.clone()

            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.0)), particle, tertiary, 0.0F, 0))
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.2000000000000002)),
                    particle,
                    tertiary,
                    0.0F,
                    0
                )
            )
            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    tertiary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.6)), particle, tertiary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.8)), particle, tertiary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-2.0)), particle, tertiary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-2.2)), particle, tertiary, 0.0F, 0))

            loc.add(0.0, 0.2, 0.0)
            loc2 = loc.clone()

            particles.add(
                ParticleMeta(
                    loc2.clone().add(loc2.direction.multiply(-1.4000000000000001)),
                    particle,
                    tertiary,
                    0.0F,
                    0
                )
            )
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.6)), particle, tertiary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-1.8)), particle, tertiary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-2.0)), particle, tertiary, 0.0F, 0))
            particles.add(ParticleMeta(loc2.clone().add(loc2.direction.multiply(-2.2)), particle, tertiary, 0.0F, 0))
        }
    }
}
