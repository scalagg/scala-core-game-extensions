package gg.tropic.game.extensions.anticheat

import dev.sim0n.anticheat.event.AntiCheatAlertEvent
import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.commons.annotations.inject.AutoBind
import gg.scala.commons.annotations.plugin.SoftDependency
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.flavor.service.ignore.IgnoreAutoScan
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

/**
 * @author GrowlyX
 * @since 12/24/2023
 */
@Service
@AutoBind
@IgnoreAutoScan
@SoftDependency("anticheat")
object AnticheatHook : Listener
{
    @Inject
    lateinit var plugin: ExtendedScalaPlugin

    data class AbstractAnticheatAlert(
        val player: Player,
        val type: AnticheatCheck,
        val label: String,
        val violations: Double
    )

    private var alertFilter: (AbstractAnticheatAlert) -> Boolean = { true }
    fun configureAlertFilter(block: (AbstractAnticheatAlert) -> Boolean)
    {
        this.alertFilter = block
    }

    @Configure
    fun configure()
    {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    fun AntiCheatAlertEvent.on()
    {
        if (alertFilter(
            AbstractAnticheatAlert(
                player = player,
                type = AnticheatCheck.valueOf(type.name),
                label = label,
                violations = violations
            )
        ))
        {
            isCancelled = true
        }
    }
}
