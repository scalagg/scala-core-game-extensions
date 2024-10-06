package gg.tropic.game.extensions.cosmetics.particles.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.particles.ParticleEffectCategory
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.nms.MinecraftReflection
import org.bukkit.Material

/**
 * @author GrowlyX
 * @since 10/27/2023
 */
object FireRingParticleEffect : RingParticleEffects
{
    override val particleType = MinecraftReflection
        .getEnumParticle("FLAME")!!

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
