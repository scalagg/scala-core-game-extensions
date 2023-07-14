package gg.tropic.game.extensions.guilds.settings

import gg.scala.basics.plugin.settings.SettingCategory
import gg.scala.basics.plugin.settings.SettingContainer.buildEntry
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.commons.annotations.plugin.SoftDependency
import gg.scala.flavor.service.Service
import gg.scala.flavor.service.ignore.IgnoreAutoScan
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
@Service
object GuildPlayerSettings : SettingCategory
{
    @JvmStatic
    val NAMESPACE = "guilds:"

    override val items = mutableListOf(
        buildEntry {
            id = "${NAMESPACE}invites"
            displayName = "Guild invites"

            clazz = StateSettingValue::class.java
            default = StateSettingValue.ENABLED

            description += "Allows you to disable"
            description += "guild invites."

            item = ItemBuilder.of(Material.LEATHER_CHESTPLATE)
        },
    )

    override fun display(player: Player) = true

    override val displayName = "Guilds"
    override val description = listOf(
        "Guild-related settings."
    )
}
