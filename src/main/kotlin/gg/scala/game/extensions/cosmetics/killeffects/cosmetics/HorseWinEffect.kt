package gg.scala.game.extensions.cosmetics.killeffects.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.PurchasableCosmetic
import gg.scala.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import gg.scala.game.extensions.cosmetics.killeffects.RideableWinEffect
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.EntityType

/**
 * @author TastyCake
 * @date 8/17/2024
 */
@Service
object HorseWinEffect : PurchasableCosmetic, RideableWinEffect(
    ImmutableResource("killeffects", "horse"), // TODO: Change to kill effects or someth
    "Horse",
    arrayListOf(
        "${CC.GRAY}Ride a Horse",
    ),
    ItemBuilder
        .of(XMaterial.SADDLE),
    EntityType.HORSE
)
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillEffectCosmeticCategory,
            this
        )
    }

    override val price = 5000f
}
