package gg.scala.game.extensions.cosmetics.projectiletrails.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.NewlyReleased
import gg.scala.game.extensions.cosmetics.projectiletrails.ProjectileTrailCosmeticCategory
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import xyz.xenondevs.particle.ParticleEffect

/**
 * @author TastyCake
 * @date 8/13/2024
 */
object MagicProjectileTrail : ProjectileTrailCosmetic, NewlyReleased
{
    override val particleEffect = ParticleEffect.PORTAL

    fun configure()
    {
        CosmeticRegistry.register(
            ProjectileTrailCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("projectiletrails", "magic")
    override val name = "Magic"
    override val description = listOf(
        "Surround your projectiles",
        "with magic effects!"
    )
    override val icon = ItemBuilder.of(XMaterial.POTION)
    override val price = 1350.0f
}
