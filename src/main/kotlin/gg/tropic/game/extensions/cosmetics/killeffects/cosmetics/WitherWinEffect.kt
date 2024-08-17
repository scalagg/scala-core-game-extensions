package gg.tropic.game.extensions.cosmetics.killeffects.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.PurchasableCosmetic
import gg.tropic.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import gg.tropic.game.extensions.cosmetics.killeffects.RideableWinEffect
import me.lucko.helper.Schedulers
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.entity.Player

/**
 * @author TastyCake
 * @date 8/17/2024
 */
@Service
object WitherWinEffect : PurchasableCosmetic, RideableWinEffect(
    ImmutableResource("killeffects", "wither"), // TODO: Change to kill effects or someth
    "Wither",
    arrayListOf(
        "${CC.GRAY}Ride a Wither",
    ),
    ItemBuilder
        .of(Material.NETHER_STAR),
    EntityType.WITHER
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

    override fun applyTo(viewers: List<Player>, killer: Player, target: Player)
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