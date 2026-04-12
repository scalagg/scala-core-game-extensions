package gg.scala.game.extensions.cosmetics.victorydances.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.PurchasableCosmetic
import gg.scala.game.extensions.cosmetics.victorydances.RideableVictoryDance
import gg.scala.game.extensions.cosmetics.victorydances.VictoryDanceCosmeticCategory
import me.lucko.helper.Schedulers
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.entity.Player

/**
 * @date 2/26/2025
 */
@Service
object DragonRiderVictoryDance : PurchasableCosmetic, RideableVictoryDance(
    ImmutableResource("victorydances", "dragonrider"),
    "Dragon Rider",
    arrayListOf(
        "${CC.GRAY}Ride a Dragon",
    ),
    ItemBuilder
        .of(XMaterial.DRAGON_EGG),
    EntityType.ENDER_DRAGON
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

    override fun applyTo(viewers: List<Player>, killer: Player, target: Player?)
    {
        super.applyTo(viewers, killer, target)

        Schedulers
            .sync()
            .runRepeating({task ->
                if (!killer.isOnline) {
                    task.closeAndReportException()
                }

                try
                {
                    killer.launchProjectile(Fireball::class.java)
                } catch (ignored: Exception) {
                    task.closeAndReportException()
                }
            }, 0L, 10L)
    }

    override val price = 5000f
}
