package gg.scala.game.extensions.cosmetics.particles.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.particles.ParticleEffectCategory
import gg.scala.game.extensions.cosmetics.particles.ParticleResolver
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material

object FireRingParticleEffect : RingParticleEffects
{
    override val particleType = ParticleResolver.resolve("FLAME", "FLAME")

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ParticleEffectCategory,
            this
        )
    }

    override val resource = ImmutableResource("particleeffects", "fire")
    override val name = "Fire Rings"
    override val description = listOf(
        "Have a ring of fire particles",
        "surround you!"
    )

    override val icon = ItemBuilder
        .of(
            Material.BLAZE_POWDER
        )
    override val price = 5000.0f
}
