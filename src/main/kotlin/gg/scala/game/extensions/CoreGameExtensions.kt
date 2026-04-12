package gg.scala.game.extensions

import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.commons.annotations.container.ContainerEnable
import gg.scala.commons.core.plugin.Plugin
import gg.scala.commons.core.plugin.PluginAuthor
import gg.scala.commons.core.plugin.PluginDependency
import gg.scala.commons.core.plugin.PluginWebsite
import gg.scala.game.extensions.profile.event.LevelUpEvent
import me.lucko.helper.Events
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.text.TextUtil
import org.bukkit.Bukkit
import org.bukkit.Sound

@Plugin(
    name = "CoreGameExtensions",
    version = "%remote%/%branch%/%id%"
)

@PluginAuthor("Scala")
@PluginWebsite("https://scala.gg")

@PluginDependency("Lemon")
@PluginDependency("ScBasics")
@PluginDependency("scala-commons")
@PluginDependency("Achievements")

@PluginDependency("ScChatTags", soft = true)
@PluginDependency("Friends", soft = true)
@PluginDependency("cloudsync", soft = true)
@PluginDependency("Tangerine", soft = true)

@PluginDependency("anticheat", soft = true)
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

                player.sendMessage("${CC.GRAY}${CC.STRIKE_THROUGH}${"-".repeat(53)}")

                TextUtil
                    .sendCenteredMessage(
                        player,
                        "${CC.B_GREEN}${CC.MAGIC}AAA${CC.RESET} ${CC.GREEN}You leveled up from level ${CC.AQUA}${it.from}${CC.GREEN} to ${CC.AQUA}${it.to}${CC.GREEN}! ${CC.B_GREEN}${CC.MAGIC}AAA"
                    )

                TextUtil
                    .sendCenteredMessage(
                        player,
                        "${CC.GRAY}Collect rewards from achievements using ${CC.WHITE}/achieve${CC.GRAY}!"
                    )

                player.sendMessage("${CC.GRAY}${CC.STRIKE_THROUGH}${"-".repeat(53)}")

                player.playSound(
                    player.location,
                    Sound.LEVEL_UP,
                    1.0f, 1.0f
                )

                Tasks.delayed(15L) {
                    player.playSound(
                        player.location,
                        Sound.FIREWORK_LAUNCH,
                        1.0f, 1.0f
                    )

                    Tasks.delayed(15L) {
                        player.playSound(
                            player.location,
                            Sound.FIREWORK_LARGE_BLAST,
                            1.0f, 1.0f
                        )
                    }
                }
            }
            .bindWith(this)
    }
}
