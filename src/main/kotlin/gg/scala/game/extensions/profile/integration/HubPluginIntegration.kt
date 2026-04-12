package gg.scala.game.extensions.profile.integration

import gg.scala.commons.annotations.plugin.SoftDependency
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.flavor.service.ignore.IgnoreAutoScan
import gg.scala.tangerine.TangerineSpigotPlugin
import gg.scala.game.extensions.profile.CorePlayerProfileService

@Service
@IgnoreAutoScan
@SoftDependency("Tangerine")
object HubPluginIntegration
{
    @Configure
    fun configure()
    {
        TangerineSpigotPlugin.instance.currencyProvider = ctx@{
            val profile = CorePlayerProfileService.find(it)
                ?: return@ctx 0

            profile.coins
        }
    }
}
