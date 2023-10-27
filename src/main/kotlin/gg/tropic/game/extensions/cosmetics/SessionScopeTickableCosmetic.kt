package gg.tropic.game.extensions.cosmetics

import gg.tropic.game.extensions.profile.profile
import org.bukkit.entity.Player
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 7/17/2023
 */
interface SessionScopeTickableCosmetic<S : CosmeticTickState> : CosmeticLike
{
    fun tick(player: Player, state: S)
    fun typeOf(): KClass<S>

    fun tickWithServedTickState(player: Player) = tick(player, serveTickState(player))

    fun serveTickState(player: Player): S
    {
        val state = player.profile
            .localTickerConfigs[resource.toString()] as S?

        if (state == null)
        {
            val instance = typeOf().java.newInstance()
            player.profile.localTickerConfigs[resource.toString()] = instance
            return instance
        }

        return state
    }
}
