package gg.scala.game.extensions.cosmetics

import gg.scala.game.extensions.profile.profile
import org.bukkit.entity.Player

interface DynamicCosmetic<T : DynamicCosmeticConfigStore> : CosmeticLike
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
