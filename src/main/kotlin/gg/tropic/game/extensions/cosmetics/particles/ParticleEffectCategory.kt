package gg.tropic.game.extensions.cosmetics.particles

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticCategory
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material

/**
 * @author GrowlyX
 * @since 10/27/2023
 */
@Service
object ParticleEffectCategory : CosmeticCategory
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.registerCategory(this)
    }

    override val resource = ImmutableResource("particleeffects")

    override val name = "Particle Effects"
    override val description = listOf(
        "Show off to other players by",
        "having particles surround you!"
    )
    override val icon = ItemBuilder
        .of(Material.BLAZE_POWDER)
}
