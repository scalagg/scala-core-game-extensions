package gg.scala.game.extensions.cosmetics.victorydances.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.PurchasableCosmetic
import gg.scala.game.extensions.cosmetics.victorydances.VictoryDanceCosmeticCategory
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.entity.Player

/**
 * @date 2/26/2025
 */
@Service
object TornadoVictoryDance : PurchasableCosmetic, SimpleVictoryDance(
    ImmutableResource("victorydances", "tornado"),
    "Tornado",
    listOf(
        "${CC.GRAY}Create a swirling tornado",
        "${CC.GRAY}of victory!"
    ),
    ItemBuilder.of(XMaterial.FEATHER)
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

    override val price = 18000f

    override fun applyTo(viewers: List<Player>, killer: Player, target: Player?)
    {
        viewers.forEach {
            it.playSound(
                it.location,
                org.bukkit.Sound.AMBIENCE_CAVE,
                0.5F,
                2.0F
            )
        }

        killer.sendMessage("${CC.AQUA}🌪️ ${CC.YELLOW}Tornado Victory Dance! ${CC.AQUA}🌪️")
    }
} 