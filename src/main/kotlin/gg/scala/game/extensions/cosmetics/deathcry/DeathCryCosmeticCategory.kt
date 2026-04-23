package gg.scala.game.extensions.cosmetics.deathcry

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticCategory
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import net.evilblock.cubed.util.bukkit.ItemBuilder

/**
 * @author Limo
 * @date 2/10/2026
 */
@Service
object DeathCryCosmeticCategory : CosmeticCategory
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.registerCategory(this)
    }

    override val resource = ImmutableResource("deathcry")

    override val name = "Death Cries"
    override val description = listOf(
        "Express yourself with custom",
        "sounds when you are eliminated!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.SKELETON_SKULL)
}
