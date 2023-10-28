package gg.tropic.game.extensions.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
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

                CosmeticRegistry.categories()
                    .flatMap { it.cosmetics() }
                    .filterIsInstance<SessionScopeTickableCosmetic<*>>()
                    .filter { it.equipped(player) }
                    .forEach {
                        it.tickWithServedTickState(player)
                    }
            }
        }, 0L, 50L, TimeUnit.MILLISECONDS)
    }
}
