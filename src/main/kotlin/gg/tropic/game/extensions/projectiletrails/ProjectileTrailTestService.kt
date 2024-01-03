package gg.tropic.game.extensions.projectiletrails

import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import java.util.function.BiConsumer

/**
 * @author GrowlyX
 * @since 1/3/2024
 */
@Service
object ProjectileTrailTestService
{
    @Inject
    lateinit var plugin: ExtendedScalaPlugin

    @Configure
    fun configure()
    {
        Events
            .subscribe(ProjectileLaunchEvent::class.java)
            .filter { event ->
                event.entity.shooter is Player &&
                    (event.entity.shooter as Player).isOp
            }
            .handler { event: ProjectileLaunchEvent ->
                ProjectileTrails.INSTANCE.createNewSpiral(
                    { location, player ->
                        ParticleBuilder(ParticleEffect.FLAME)
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
