package gg.scala.game.extensions.cosmetics.killeffects.cosmetics

import com.cryptomorin.xseries.XMaterial
import de.slikey.effectlib.effect.BigBangEffect
import de.slikey.effectlib.util.DynamicLocation
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.NewlyReleased
import gg.scala.game.extensions.cosmetics.PurchasableCosmetic
import gg.scala.game.extensions.cosmetics.killeffects.Effects
import gg.scala.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import gg.scala.game.extensions.projectiletrails.CompatBigBangEffect
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

@Service
object FireworksKillEffect : KillEffect, PurchasableCosmetic, NewlyReleased
{
    override fun applyTo(viewers: List<Player>, killer: Player, target: Player)
    {
        val effect = gg.scala.game.extensions.projectiletrails.CompatBigBangEffect(Effects.get())
        effect.dynamicOrigin = DynamicLocation(
            killer.location.clone()
                .add(0.0, 5.0, 0.0)
        )
        effect.start()
    }

    @Configure
    fun configure()
    {

    }

    override val price = 3500F
    override val resource = ImmutableResource("killeffects", "fireworks")
    override val name = "Fireworks"
    override val description = listOf(
        "Celebrate with fireworks!"
    )
    override val icon = ItemBuilder.of(XMaterial.FIREWORK_ROCKET)
}
