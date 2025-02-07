package gg.scala.game.extensions.cosmetics.particles

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticCategory
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.particles.cosmetics.FireRingParticleEffect
import gg.scala.game.extensions.cosmetics.particles.cosmetics.SnowRingParticleEffect
import net.evilblock.cubed.util.ServerVersion
import net.evilblock.cubed.util.bukkit.ItemBuilder

/**
 * @author GrowlyX
 * @since 10/27/2023
 */
@Service
object ParticleEffectCategory : CosmeticCategory
{
    @Configure
    fun configure()
    {
        if (ServerVersion.getVersion().isOlderThanOrEquals(ServerVersion.v1_8_8))
        {
            CosmeticRegistry.registerCategory(this)

            FireRingParticleEffect.configure()
            SnowRingParticleEffect.configure()
        }
    }

    override val resource = ImmutableResource("particleeffects")

    override val name = "Particle Effects"
    override val description = listOf(
        "Show off to other players by",
        "having particles surround you!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.BLAZE_POWDER)
}
