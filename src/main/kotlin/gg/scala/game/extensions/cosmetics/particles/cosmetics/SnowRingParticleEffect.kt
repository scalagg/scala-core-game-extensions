package gg.scala.game.extensions.cosmetics.particles.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.particles.ParticleEffectCategory
import gg.scala.game.extensions.cosmetics.particles.ParticleResolver
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material

object SnowRingParticleEffect : RingParticleEffects
{
    override val particleType = ParticleResolver.resolve("SNOW_SHOVEL", "SNOWFLAKE")

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ParticleEffectCategory,
            this
        )
    }

    override val resource = ImmutableResource("particleeffects", "snow")
    override val name = "Snow Rings"
    override val description = listOf(
        "Be frosty the snowman! Have rings",
        "of snow surround you!"
    )

    override val icon = ItemBuilder
        .of(
            Material.SNOW_BALL
        )
    override val price = 5000.0f
}
