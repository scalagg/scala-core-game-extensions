package gg.scala.game.extensions.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.profile.CorePlayerProfileService
import me.lucko.helper.utils.Players
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author GrowlyX
 * @since 10/26/2023
 */
@Service
object CosmeticTickService
{
    @Configure
    fun configure()
    {
        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.scheduleAtFixedRate({
            runCatching {
                if (!CosmeticLocalConfig.enableCosmeticResources)
                {
                    return@scheduleAtFixedRate
                }

                for (player in Players.all())
                {
                    if (CosmeticCommand.preventCosmeticUsage(player))
                    {
                        continue
                    }

                    val profile = CorePlayerProfileService
                        .find(player)
                        ?: return@runCatching

                    CosmeticRegistry.categories()
                        .flatMap { it.cosmetics() }
                        .filterIsInstance<SessionScopeTickableCosmetic<*>>()
                        .filter { profile.equippedCosmetics.contains(it.resource) }
                        .forEach {
                            it.tickWithServedTickState(player)
                        }
                }
            }
        }, 0L, 50L, TimeUnit.MILLISECONDS)
    }
}
