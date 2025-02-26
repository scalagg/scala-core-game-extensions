package gg.scala.game.extensions.cosmetics.victorydances

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticCategory
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import net.evilblock.cubed.util.bukkit.ItemBuilder

/**
 * @author Topu
 * @date 2/26/2025
 */
@Service
object VictoryDanceCosmeticCategory : CosmeticCategory
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.registerCategory(this)
    }

    override val resource = ImmutableResource("victorydances")

    override val name = "Victory Dances"
    override val description = listOf(
        "Show off to other players by",
        "using custom victory dance on them!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.DRAGON_EGG)
}
