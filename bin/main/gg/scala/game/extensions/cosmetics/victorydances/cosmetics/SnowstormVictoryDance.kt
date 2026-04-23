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
import org.bukkit.Sound
import org.bukkit.entity.Player

/**
 * @author Pawful
 * @date 2/26/2025
 */
@Service
object SnowstormVictoryDance : PurchasableCosmetic, SimpleVictoryDance(
    ImmutableResource("victorydances", "snowstorm"),
    "Snowstorm",
    listOf(
        "${CC.GRAY}Create a blizzard of",
        "${CC.GRAY}victory around you!"
    ),
    ItemBuilder.of(XMaterial.SNOWBALL)
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

    override val price = 14000f

    override fun applyTo(viewers: List<Player>, killer: Player, target: Player?)
    {
        viewers.forEach {
            it.playSound(
                it.location,
                org.bukkit.Sound.AMBIENCE_CAVE,
                0.7F,
                1.8F
            )
        }
        
        killer.sendMessage("${CC.AQUA}❄️ ${CC.YELLOW}Snowstorm Victory Dance! ${CC.AQUA}❄️")
    }
} 