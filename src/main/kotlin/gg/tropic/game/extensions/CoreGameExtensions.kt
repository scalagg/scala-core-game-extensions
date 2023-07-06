package gg.tropic.game.extensions

import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.commons.core.plugin.Plugin
import gg.scala.commons.core.plugin.PluginAuthor
import gg.scala.commons.core.plugin.PluginDependency
import gg.scala.commons.core.plugin.PluginWebsite

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
}
