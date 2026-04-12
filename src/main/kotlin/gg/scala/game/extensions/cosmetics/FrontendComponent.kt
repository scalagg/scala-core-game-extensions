package gg.scala.game.extensions.cosmetics

import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder

interface FrontendComponent
{
    val name: String
    val description: List<String>
    val icon: ItemBuilder

    fun create() = ItemBuilder
        .copyOf(icon)
        .name("${CC.GREEN}$name${
            if (this is NewlyReleased) "${CC.B_GOLD} (NEW!)" else ""
        }")
        .setLore(
            description.map { "${CC.GRAY}$it" }
        )
}
