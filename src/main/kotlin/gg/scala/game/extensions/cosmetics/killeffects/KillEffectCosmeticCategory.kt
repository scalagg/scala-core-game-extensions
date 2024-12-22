package gg.scala.game.extensions.cosmetics.killeffects

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
 * @since 10/26/2023
 */
@Service
object KillEffectCosmeticCategory : CosmeticCategory
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.registerCategory(this)
    }

    override val resource = ImmutableResource("killeffects")

    override val name = "Kill Effects"
    override val description = listOf(
        "Show off to other players by",
        "using custom kill effects on them!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.REDSTONE_BLOCK)
}
