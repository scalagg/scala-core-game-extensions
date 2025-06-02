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
object AngelWingsEffect : WingParticleEffects
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            WingCosmeticCategory,
            this
        )
    }

    override val primary: Color = Color.WHITE
    override val secondary: Color = Color.fromRGB(255, 215, 0) // Gold
    override val tertiary: Color = Color.fromRGB(255, 255, 224) // Light yellow

    override val name = "Angel"
    override val description = listOf(
        "Fly with the pure wings",
        "of a heavenly angel!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.FEATHER)

    override val resource = ImmutableResource("wings", "angel")
    override val price = 40_000.0f
} 