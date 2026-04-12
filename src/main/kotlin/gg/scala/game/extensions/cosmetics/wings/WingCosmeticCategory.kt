package gg.scala.game.extensions.cosmetics.wings

import com.cryptomorin.xseries.XMaterial
import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticCategory
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import me.lucko.helper.Events
import net.evilblock.cubed.util.bukkit.EventUtils
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.event.player.PlayerMoveEvent

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
                    .getSingleEquippedAs<WingParticleEffects>(this, it.player)
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
        .of(XMaterial.LILY_PAD)
}
