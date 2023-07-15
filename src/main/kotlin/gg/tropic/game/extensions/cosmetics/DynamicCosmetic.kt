package gg.tropic.game.extensions.cosmetics

import gg.tropic.game.extensions.profile.profile
import org.bukkit.entity.Player

/**
 * Delegates a passed in [CosmeticLike] and adds additional
 * functionality to customize cosmetic settings (persistent
 * configurations) and equip cosmetics with respect to the given
 * player configuration.
 *
 * @author GrowlyX
 * @since 7/15/2023
 */
abstract class DynamicCosmetic<T : DynamicCosmeticConfigStore>(
    private val cosmetic: CosmeticLike
) : CosmeticLike by cosmetic
{
    override fun equip(player: Player)
    {
        equip(player, serveConfiguration(player))
    }

    open fun serveConfiguration(player: Player): T
    {
        player.profile.cosmeticConfigs
            .putIfAbsent(
                resource.toString(),
                buildDefaultConfiguration()
            )

        return player.profile
            .cosmeticConfigs[resource.toString()]!! as T
    }

    abstract fun openEditor(player: Player)

    abstract fun buildDefaultConfiguration(): T
    abstract fun equip(player: Player, configuration: T)
}
