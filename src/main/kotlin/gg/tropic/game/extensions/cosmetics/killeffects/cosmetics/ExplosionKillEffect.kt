package gg.tropic.game.extensions.cosmetics.killeffects.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import xyz.xenondevs.particle.ParticleEffect

/**
 * @author GrowlyX
 * @since 12/22/2023
 */
@Service
object ExplosionKillEffect : KillEffect
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillEffectCosmeticCategory,
            this
        )
    }

    override fun applyTo(viewers: List<Player>, killer: Player, target: Player)
    {
        ParticleEffect.EXPLOSION_HUGE
            .display(
                target.location,
                *viewers.toTypedArray()
            )

        viewers.forEach {
            it.playSound(
                it.location,
                Sound.EXPLODE,
                1.0F,
                0.6F
            )
        }

        val location = killer.location
            .direction
            .clone()
            .multiply(1.5)
            .setY(0.7)

        Tasks.delayed(25L) {
            viewers.forEach { viewer ->
                viewer.hidePlayer(target)
            }
        }

        target.velocity = location
    }

    override val resource = ImmutableResource("killeffects", "explosion")
    override val name = "Explosion"
    override val description = listOf(
        "Blow up your opponent on when",
        "you kill them!"
    )
    override val icon = ItemBuilder.of(Material.FIREBALL)
    override val price = 500.0f
}
