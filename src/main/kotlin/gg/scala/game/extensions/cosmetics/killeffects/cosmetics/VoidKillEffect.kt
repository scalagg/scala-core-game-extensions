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

@Service
object VoidKillEffect : KillEffect, PurchasableCosmetic
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
        val location = target.location.clone().add(0.0, 0.5, 0.0)

        
        ParticleEffect.PORTAL
            .display(
                location,
                *viewers.toTypedArray()
            )

        
        ParticleEffect.SMOKE_LARGE
            .display(
                location.clone().add(0.0, 1.0, 0.0),
                *viewers.toTypedArray()
            )

        viewers.forEach {
            it.playSound(
                it.location,
                Sound.WITHER_SPAWN,
                1.0F,
                0.3F
            )
        }

        Tasks.delayed(20L) {
            viewers.forEach { viewer ->
                viewer.hidePlayer(target)
            }
        }
    }

    override val resource = ImmutableResource("killeffects", "void")
    override val name = "Void"
    override val description = listOf(
        "Send your enemy into the",
        "dark depths of the void!"
    )
    override val icon = ItemBuilder.of(XMaterial.ENDER_PEARL)
    override val price = 15000.0f
} 