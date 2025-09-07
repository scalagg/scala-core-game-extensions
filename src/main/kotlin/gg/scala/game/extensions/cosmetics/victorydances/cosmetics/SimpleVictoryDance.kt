package gg.scala.game.extensions.cosmetics.victorydances.cosmetics

import gg.scala.game.extensions.cosmetics.ImmutableResource
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.entity.Player

/**
 * @author Pawful
 * @date 2/26/2025
 */
open class SimpleVictoryDance(
    resourceI: ImmutableResource,
    nameI: String,
    descriptionI: List<String>,
    iconI: ItemBuilder
) : VictoryDance
{
    override val resource = resourceI
    override val name = nameI
    override val description = descriptionI
    override val icon = iconI

    override fun applyTo(viewers: List<Player>, killer: Player, target: Player)
    {
        Tasks.delayed(20L) {
            viewers.forEach { viewer ->
                viewer.hidePlayer(target)
            }
        }
    }
} 