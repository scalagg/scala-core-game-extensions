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

@Service
object RainbowWingsEffect : WingParticleEffects
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            WingCosmeticCategory,
            this
        )
    }

    override val primary: Color = Color.fromRGB(255, 0, 128) 
    override val secondary: Color = Color.fromRGB(128, 255, 0) 
    override val tertiary: Color = Color.fromRGB(0, 128, 255) 

    override val name = "Rainbow"
    override val description = listOf(
        "Spread your colorful wings",
        "across the rainbow spectrum!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.NETHER_STAR)

    override val resource = ImmutableResource("wings", "rainbow")
    override val price = 50_000.0f
} 