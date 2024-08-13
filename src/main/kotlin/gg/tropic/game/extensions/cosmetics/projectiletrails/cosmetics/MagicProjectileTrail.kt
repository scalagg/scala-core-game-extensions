package gg.tropic.game.extensions.cosmetics.projectiletrails.cosmetics

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
 * @author TastyCake
 * @date 8/13/2024
 */
@Service
object MagicProjectileTrail : ProjectileTrailCosmetic, NewlyReleased
{
    override val particleEffect = ParticleEffect.PORTAL

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ProjectileTrailCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("projectiletrails", "magic")
    override val name = "Portal"
    override val description = listOf(
        "Surround your projectiles",
        "with magic effects!"
    )
    override val icon = ItemBuilder.of(Material.POTION)
    override val price = 1350.0f
}