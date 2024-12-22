package gg.scala.game.extensions.cosmetics.armor

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticCategory
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
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

    override val name = "Suits"
    override val description = listOf(
        "Wear suit sets in the hub",
        "or at spawn in your favorite",
        "gamemode!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.GOLDEN_CHESTPLATE)
}
