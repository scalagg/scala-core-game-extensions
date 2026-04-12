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
object ShadowWingsEffect : WingParticleEffects
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            WingCosmeticCategory,
            this
        )
    }

    override val primary: Color = Color.fromRGB(25, 25, 25) 
    override val secondary: Color = Color.fromRGB(75, 0, 130) 
    override val tertiary: Color = Color.BLACK

    override val name = "Shadow"
    override val description = listOf(
        "Embrace the darkness with",
        "wings of pure shadow!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.COAL)

    override val resource = ImmutableResource("wings", "shadow")
    override val price = 30_000.0f
} 