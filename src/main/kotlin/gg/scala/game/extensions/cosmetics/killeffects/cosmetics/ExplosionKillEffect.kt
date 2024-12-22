package gg.scala.game.extensions.cosmetics.killeffects.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.PurchasableCosmetic
import gg.scala.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import gg.scala.game.extensions.cosmetics.utilities.DeathAnimatorUtilities
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
object ExplosionKillEffect : KillEffect, PurchasableCosmetic
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

        target.velocity = location

        Tasks.delayed(20L) {
            viewers.forEach { viewer ->
                viewer.hidePlayer(target)
            }
        }
    }

    override val resource = ImmutableResource("killeffects", "explosion")
    override val name = "Explosion"
    override val description = listOf(
        "Watch your Opponent implode",
        "when you kill them!"
    )
    override val icon = ItemBuilder.of(XMaterial.FIRE_CHARGE)
    override val price = 500.0f
}
