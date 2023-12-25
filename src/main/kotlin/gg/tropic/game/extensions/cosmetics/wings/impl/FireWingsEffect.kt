package gg.tropic.game.extensions.cosmetics.wings.impl

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.wings.WingCosmeticCategory
import gg.tropic.game.extensions.cosmetics.wings.WingParticleEffects
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Color
import org.bukkit.Material

/**
 * @author GrowlyX
 * @since 12/24/2023
 */
@Service
object FireWingsEffect : WingParticleEffects
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            WingCosmeticCategory,
            this
        )
    }

    override val primary: Color = Color.RED
    override val secondary: Color = Color.ORANGE
    override val tertiary: Color = Color.YELLOW

    override val name = "Fire"
    override val description = listOf(
        "Have large wings of flames",
        "trail behind you!"
    )
    override val icon = ItemBuilder
        .of(Material.BLAZE_POWDER)

    override val resource = ImmutableResource("wings", "fire")
    override val price = 20_000.0f
}
