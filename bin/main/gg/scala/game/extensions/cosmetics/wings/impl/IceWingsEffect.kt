package gg.scala.game.extensions.cosmetics.wings.impl

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.wings.WingCosmeticCategory
import gg.scala.game.extensions.cosmetics.wings.WingParticleEffects
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Color

/**
 * @author Pawful
 * @since 06/01/2025
 */
@Service
object IceWingsEffect : WingParticleEffects
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            WingCosmeticCategory,
            this
        )
    }

    override val primary: Color = Color.AQUA
    override val secondary: Color = Color.WHITE
    override val tertiary: Color = Color.fromRGB(173, 216, 230) // Light blue

    override val name = "Ice"
    override val description = listOf(
        "Spread your frozen wings",
        "and leave a trail of ice!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.ICE)

    override val resource = ImmutableResource("wings", "ice")
    override val price = 25_000.0f
} 