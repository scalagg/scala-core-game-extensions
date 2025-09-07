package gg.scala.game.extensions.cosmetics.particles.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.particles.ParticleEffectCategory
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.nms.MinecraftReflection

/**
 * @author Pawful
 * @since 06/01/2025
 */
@Service
object StarTrailParticleEffect : RingParticleEffects
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ParticleEffectCategory,
            this
        )
    }

    override val name = "Star Trail"
    override val description = listOf(
        "${CC.GRAY}Leave a magical trail of",
        "${CC.GRAY}twinkling stars!"
    )
    override val icon = ItemBuilder.of(XMaterial.NETHER_STAR)
    override val resource = ImmutableResource("particles", "startrail")
    override val price = 18000f

    override val particleType = MinecraftReflection.getEnumParticle("FIREWORKS_SPARK")!!
} 