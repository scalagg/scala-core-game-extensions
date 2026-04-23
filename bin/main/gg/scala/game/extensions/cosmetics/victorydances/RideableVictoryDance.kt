package gg.scala.game.extensions.cosmetics.victorydances

import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.victorydances.cosmetics.VictoryDance
import me.lucko.helper.Events
import me.lucko.helper.Schedulers
import me.lucko.helper.terminable.composite.CompositeTerminable
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author Topu
 * @date 2/26/2025
 */
open class RideableVictoryDance(
    resourceI: ImmutableResource,
    nameI: String,
    descriptionI: ArrayList<String>,
    iconI: ItemBuilder,
    var entityType: EntityType
) : VictoryDance
{
    override fun applyTo(viewers: List<Player>, killer: Player, target: Player?)
    {
        val terminable = CompositeTerminable.create()
        val dragon = killer.location.world.spawnEntity(killer.location, entityType)
        dragon.passenger = killer

        terminable.with {
            dragon.remove()
        }

        Schedulers
            .async()
            .runRepeating({_ ->
                dragon.passenger = killer
                val direction = killer.location.direction.normalize().multiply(1) // Make faster by multiplying

                dragon.velocity = direction
            }, 0L, 1L)
            .bindWith(terminable)

        Events.subscribe(PlayerQuitEvent::class.java)
            .filter { it.player.uniqueId == killer.uniqueId }
            .handler { terminable.closeAndReportException() }
            .bindWith(terminable)

        if (target != null)
        {
            Tasks.delayed(20L) {
                viewers.forEach { viewer ->
                    viewer.hidePlayer(target)
                }
            }
        }
    }

    override val resource = resourceI
    override val name = nameI
    override val description = descriptionI
    override val icon = iconI
}