package gg.tropic.game.extensions.cosmetics

import gg.tropic.game.extensions.profile.profile
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/17/2023
 */
interface TickableCosmetic<S : CosmeticTickState> : CosmeticLike
{
    fun tick(player: Player, state: S)

    fun serveTickState(player: Player): S
    {
        return player.profile
            .localTickerConfigs[resource.toString()] as S
    }
}
