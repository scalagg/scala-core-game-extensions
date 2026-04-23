package gg.scala.game.extensions.cosmetics.killeffects.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.PurchasableCosmetic
import gg.scala.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.Sound
import org.bukkit.entity.Player
import xyz.xenondevs.particle.ParticleEffect
import kotlin.random.Random

/**
 * @author Pawful
 * @since 06/01/2025
 */
@Service
object ConfettiKillEffect : KillEffect, PurchasableCosmetic
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
        val location = target.location.clone().add(0.0, 1.0, 0.0)
        
        // Create confetti explosion with fireworks effect
        ParticleEffect.FIREWORKS_SPARK
            .display(
                location.clone().add(0.0, 1.0, 0.0),
                *viewers.toTypedArray()
            )
            
        // Add some heart particles for celebration
        ParticleEffect.HEART
            .display(
                location.clone().add(0.0, 1.5, 0.0),
                *viewers.toTypedArray()
            )

        viewers.forEach {
            it.playSound(
                it.location,
                Sound.LEVEL_UP,
                1.0F,
                1.5F
            )
        }

        Tasks.delayed(20L) {
            viewers.forEach { viewer ->
                viewer.hidePlayer(target)
            }
        }
    }

    override val resource = ImmutableResource("killeffects", "confetti")
    override val name = "Confetti"
    override val description = listOf(
        "Celebrate your victory with",
        "a colorful confetti explosion!"
    )
    override val icon = ItemBuilder.of(XMaterial.FIREWORK_ROCKET)
    override val price = 8000.0f
} 