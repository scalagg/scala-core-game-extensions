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
object DragonWingsEffect : WingParticleEffects
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            WingCosmeticCategory,
            this
        )
    }

    override val primary: Color = Color.fromRGB(220, 20, 60) // Crimson
    override val secondary: Color = Color.fromRGB(255, 215, 0) // Gold
    override val tertiary: Color = Color.fromRGB(139, 69, 19) // Saddle brown

    override val name = "Dragon"
    override val description = listOf(
        "Soar with the mighty wings",
        "of an ancient dragon!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.DRAGON_HEAD)

    override val resource = ImmutableResource("wings", "dragon")
    override val price = 45_000.0f
} 