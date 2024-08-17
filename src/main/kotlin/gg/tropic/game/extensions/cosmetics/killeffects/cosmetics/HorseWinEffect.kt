package gg.tropic.game.extensions.cosmetics.killeffects.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.PurchasableCosmetic
import gg.tropic.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import gg.tropic.game.extensions.cosmetics.killeffects.RideableWinEffect
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
        .of(Material.SADDLE),
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