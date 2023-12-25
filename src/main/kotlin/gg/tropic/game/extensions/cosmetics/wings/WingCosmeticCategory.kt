package gg.tropic.game.extensions.cosmetics.wings

import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticCategory
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import me.lucko.helper.Events
import net.evilblock.cubed.util.bukkit.EventUtils
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.event.player.PlayerMoveEvent

/**
 * @author GrowlyX
 * @since 12/23/2023
 */
@Service
object WingCosmeticCategory : CosmeticCategory
{
    @Inject
    lateinit var plugin: ExtendedScalaPlugin

    @Configure
    fun configure()
    {
        CosmeticRegistry.registerCategory(this)

        Events.subscribe(PlayerMoveEvent::class.java)
            .filter(EventUtils::hasPlayerMoved)
            .handler {
                CosmeticRegistry
                    .getSingleEquipped<WingParticleEffects>(this, it.player)
                    ?.apply {
                        serveTickState(it.player).lastMovement = System.currentTimeMillis()
                    }
            }
            .bindWith(plugin)
    }

    override val resource = ImmutableResource("wings")

    override val name = "Wings"
    override val description = listOf(
        "Wear wings like a cute little",
        "butterfly!"
    )
    override val icon = ItemBuilder
        .of(Material.WATER_LILY)
}
