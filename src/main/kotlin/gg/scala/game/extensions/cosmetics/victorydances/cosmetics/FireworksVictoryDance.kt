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
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.FireworkMeta

/**
 * @author Pawful
 * @date 2/26/2025
 */
@Service
object FireworksVictoryDance : PurchasableCosmetic, SimpleVictoryDance(
    ImmutableResource("victorydances", "fireworks"),
    "Fireworks",
    listOf(
        "${CC.GRAY}Celebrate with a spectacular",
        "${CC.GRAY}fireworks display!"
    ),
    ItemBuilder.of(XMaterial.FIREWORK_ROCKET)
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

    override val price = 15000f

    override fun applyTo(viewers: List<Player>, killer: Player, target: Player)
    {
        // Simple fireworks effect using particles
        viewers.forEach {
            it.playSound(
                it.location,
                org.bukkit.Sound.FIREWORK_BLAST,
                1.0F,
                1.0F
            )
        }
        
        killer.sendMessage("${CC.GOLD}🎆 ${CC.YELLOW}Fireworks Victory Dance! ${CC.GOLD}🎆")
    }
} 