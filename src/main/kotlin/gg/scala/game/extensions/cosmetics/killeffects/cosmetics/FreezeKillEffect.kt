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
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import xyz.xenondevs.particle.ParticleEffect

/**
 * @author Pawful
 * @since 06/01/2025
 */
@Service
object FreezeKillEffect : KillEffect, PurchasableCosmetic
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
        val location = target.location.clone()
        
        // Create ice block at target location temporarily
        val originalBlock = location.block.type
        location.block.type = Material.ICE
        
        // Create freezing particle effects
        ParticleEffect.CLOUD
            .display(
                location.clone().add(0.0, 1.0, 0.0),
                *viewers.toTypedArray()
            )

        viewers.forEach {
            it.playSound(
                it.location,
                Sound.GLASS,
                1.0F,
                0.5F
            )
        }

        // Remove ice block after delay and hide player
        Tasks.delayed(40L) {
            location.block.type = originalBlock
            viewers.forEach { viewer ->
                viewer.hidePlayer(target)
            }
        }
    }

    override val resource = ImmutableResource("killeffects", "freeze")
    override val name = "Freeze"
    override val description = listOf(
        "Freeze your opponent solid",
        "with icy cold death!"
    )
    override val icon = ItemBuilder.of(XMaterial.ICE)
    override val price = 12000.0f
} 