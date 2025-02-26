package gg.scala.game.extensions.cosmetics.victorydances.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.PurchasableCosmetic
import gg.scala.game.extensions.cosmetics.victorydances.RideableVictoryDance
import gg.scala.game.extensions.cosmetics.victorydances.VictoryDanceCosmeticCategory
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.entity.EntityType

/**
 * @author Topu
 * @date 2/26/2025
 */
@Service
object HogRiderVictoryDance : PurchasableCosmetic, RideableVictoryDance(
    ImmutableResource("victorydances", "hogrider"),
    "Hog Rider",
    arrayListOf(
        "${CC.GRAY}HOG RIDAAAA",
    ),
    ItemBuilder
        .of(XMaterial.COOKED_PORKCHOP),
    EntityType.PIG
)
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            VictoryDanceCosmeticCategory,
            this
        )
    }

    override val price = 5000f
}