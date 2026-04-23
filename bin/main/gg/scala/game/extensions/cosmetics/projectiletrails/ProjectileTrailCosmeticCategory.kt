package gg.scala.game.extensions.cosmetics.projectiletrails

import com.cryptomorin.xseries.XMaterial
import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticCategory
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.NewlyReleased
import gg.scala.game.extensions.cosmetics.projectiletrails.cosmetics.FlameProjectileTrail
import gg.scala.game.extensions.cosmetics.projectiletrails.cosmetics.MagicProjectileTrail
import gg.scala.game.extensions.cosmetics.projectiletrails.cosmetics.ProjectileTrailCosmetic
import gg.scala.game.extensions.cosmetics.projectiletrails.cosmetics.SnowProjectileTrail
import gg.scala.game.extensions.projectiletrails.ProjectileTrails
import me.lucko.helper.Events
import net.evilblock.cubed.util.ServerVersion
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import xyz.xenondevs.particle.ParticleBuilder

/**
 * @author GrowlyX
 * @since 1/17/2024
 */
@Service
object ProjectileTrailCosmeticCategory : CosmeticCategory, NewlyReleased
{
    @Inject
    lateinit var plugin: ExtendedScalaPlugin

    @Configure
    fun configure()
    {
        if (ServerVersion.getVersion().isOlderThanOrEquals(ServerVersion.v1_8_8) || ServerVersion.getVersion().isNewerThan(ServerVersion.v1_20_R3))
        {
            CosmeticRegistry.registerCategory(this)

            FlameProjectileTrail.configure()
            MagicProjectileTrail.configure()
            SnowProjectileTrail.configure()

            Events
                .subscribe(ProjectileLaunchEvent::class.java)
                .filter { event ->
                    event.entity.shooter is Player &&
                        event.entityType == EntityType.SNOWBALL ||
                        event.entityType == EntityType.ARROW
                }
                .handler { event ->
                    val cosmetic = CosmeticRegistry
                        .getSingleEquippedAs<ProjectileTrailCosmetic>(
                            ProjectileTrailCosmeticCategory,
                            event.entity.shooter as Player
                        )
                        ?: return@handler

                    gg.scala.game.extensions.projectiletrails.ProjectileTrails.INSTANCE.createNewSpiral(
                        { location, player ->
                            ParticleBuilder(cosmetic.particleEffect)
                                .setLocation(location)
                                .display(player)
                        },
                        event.entity,
                        event.entity.shooter as Player
                    )
                }
                .bindWith(plugin)
        }
    }

    override val resource = ImmutableResource("projectiletrails")

    override val name = "Projectile Trails"
    override val description = listOf(
        "Highlight your projectiles with",
        "fancy spiral particles mid-air!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.BOW)
}
