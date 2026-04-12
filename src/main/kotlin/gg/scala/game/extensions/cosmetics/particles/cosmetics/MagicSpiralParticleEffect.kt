package gg.scala.game.extensions.cosmetics.particles.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.particles.ParticleEffectCategory
import gg.scala.game.extensions.cosmetics.particles.ParticleResolver
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder

@Service
object MagicSpiralParticleEffect : RingParticleEffects
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ParticleEffectCategory,
            this
        )
    }

    override val name = "Magic Spiral"
    override val description = listOf(
        "${CC.GRAY}Surround yourself with a",
        "${CC.GRAY}magical spiraling aura!"
    )
    override val icon = ItemBuilder.of(XMaterial.ENCHANTED_BOOK)
    override val resource = ImmutableResource("particles", "magicspiral")
    override val price = 22000f

    override val particleType = ParticleResolver.resolve("ENCHANTMENT_TABLE", "ENCHANT")
} 
