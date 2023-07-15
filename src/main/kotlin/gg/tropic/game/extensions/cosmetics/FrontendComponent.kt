package gg.tropic.game.extensions.cosmetics

import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
interface FrontendComponent
{
    val name: String
    val description: List<String>
    val icon: ItemBuilder

    fun create() = ItemBuilder
        .copyOf(icon)
        .name("${CC.GREEN}$name")
        .setLore(
            description.map { "${CC.GRAY}$it" }
        )
}
