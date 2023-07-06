package gg.tropic.game.extensions

import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.commons.annotations.container.ContainerEnable
import gg.scala.commons.core.plugin.Plugin
import gg.scala.commons.core.plugin.PluginAuthor
import gg.scala.commons.core.plugin.PluginDependency
import gg.scala.commons.core.plugin.PluginWebsite
import gg.tropic.game.extensions.profile.event.LevelUpEvent
import me.lucko.helper.Events
import net.evilblock.cubed.util.CC
import org.bukkit.Bukkit
import org.bukkit.Sound

/**
 * @author GrowlyX
 * @since 7/5/2023
 */
@Plugin(
    name = "CoreGameExtensions",
    version = "%remote%/%branch%/%id%"
)

@PluginAuthor("Tropic")
@PluginWebsite("https://tropic.gg")

@PluginDependency("Lemon")
@PluginDependency("scala-commons")
@PluginDependency("Achievements")

@PluginDependency("Friends", soft = true)
@PluginDependency("ScBasics", soft = true)
@PluginDependency("cloudsync", soft = true)
class CoreGameExtensions : ExtendedScalaPlugin()
{
    @ContainerEnable
    fun containerEnable()
    {
        Events
            .subscribe(LevelUpEvent::class.java)
            .handler {
                val player = Bukkit.getPlayer(it.profile.identifier)
                    ?: return@handler

                player.sendMessage(arrayOf(
                    "${CC.GRAY}${CC.STRIKE_THROUGH}${"-".repeat(45)}",
                    "${CC.GREEN}You leveled up from level ${CC.PRI}${it.from}${CC.GREEN} to ${CC.PRI}${it.to}${CC.GREEN}!",
                    "${CC.GRAY}${CC.STRIKE_THROUGH}${"-".repeat(45)}"
                ))

                player.playSound(
                    player.location,
                    Sound.LEVEL_UP,
                    1.0f, 1.0f
                )
            }
            .bindWith(this)
    }
}
