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
object FlameAuraParticleEffect : RingParticleEffects
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ParticleEffectCategory,
            this
        )
    }

    override val name = "Flame Aura"
    override val description = listOf(
        "${CC.GRAY}Surround yourself with a",
        "${CC.GRAY}blazing ring of fire!"
    )
    override val icon = ItemBuilder.of(XMaterial.BLAZE_POWDER)
    override val resource = ImmutableResource("particles", "flameaura")
    override val price = 25000f

    override val particleType = ParticleResolver.resolve("FLAME", "FLAME")
} 
