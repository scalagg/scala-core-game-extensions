package gg.tropic.game.extensions.cosmetics.projectiletrails.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.NewlyReleased
import gg.tropic.game.extensions.cosmetics.projectiletrails.ProjectileTrailCosmeticCategory
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import xyz.xenondevs.particle.ParticleEffect

/**
 * @author GrowlyX
 * @since 1/17/2024
 */
@Service
object FlameProjectileTrail : ProjectileTrailCosmetic, NewlyReleased
{
    override val particleEffect = ParticleEffect.FLAME

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ProjectileTrailCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("projectiletrails", "flame")
    override val name = "Flame"
    override val description = listOf(
        "Surround your projectiles",
        "with swirls of flames!"
    )
    override val icon = ItemBuilder.of(XMaterial.BLAZE_POWDER)
    override val price = 1350.0f
}
