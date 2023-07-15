package gg.tropic.game.extensions.cosmetics.armor

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticCategory
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
@Service
object ArmorCosmeticCategory : CosmeticCategory
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.registerCategory(this)
    }

    override val resource = ImmutableResource("armor")

    override val name = "Armor"
    override val description = listOf(
        "Wear armor sets in the hub",
        "or at spawn on your favorite",
        "gamemode!"
    )
    override val icon = ItemBuilder
        .of(Material.GOLD_CHESTPLATE)
}
