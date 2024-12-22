package gg.scala.game.extensions.cosmetics

import gg.scala.game.extensions.profile.profile
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
interface DynamicCosmetic<T : gg.scala.game.extensions.cosmetics.DynamicCosmeticConfigStore> : CosmeticLike
{
    override fun equip(player: Player)
    {
        equip(player, serveConfiguration(player))
    }

    fun serveConfiguration(player: Player): T
    {
        player.profile.cosmeticConfigs
            .putIfAbsent(
                resource.toString(),
                buildDefaultConfiguration()
            )

        return player.profile
            .cosmeticConfigs[resource.toString()]!! as T
    }

    fun openEditor(player: Player)

    fun buildDefaultConfiguration(): T
    fun equip(player: Player, configuration: T)
}
