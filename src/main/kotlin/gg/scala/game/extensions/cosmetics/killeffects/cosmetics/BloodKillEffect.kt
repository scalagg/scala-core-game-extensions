package gg.scala.game.extensions.cosmetics.killeffects.cosmetics

import com.cryptomorin.xseries.XMaterial
import de.slikey.effectlib.effect.BleedEffect
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.PurchasableCosmetic
import gg.scala.game.extensions.cosmetics.killeffects.Effects
import gg.scala.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

@Service
object BloodKillEffect : KillEffect, PurchasableCosmetic
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
        val bleed = BleedEffect(Effects.get())
        bleed.entity = target
        bleed.callback = Runnable {
            viewers.forEach { viewer ->
                viewer.hidePlayer(target)
            }
        }
    }

    override val resource = ImmutableResource("killeffects", "blood")
    override val name = "Blood"
    override val description = listOf(
        "Cause a pulse-shower of blood",
        "after beating on your opponent."
    )
    override val icon = ItemBuilder.of(XMaterial.REDSTONE)
    override val price = 750.0f
}
