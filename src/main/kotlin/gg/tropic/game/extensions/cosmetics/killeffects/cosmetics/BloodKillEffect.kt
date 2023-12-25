package gg.tropic.game.extensions.cosmetics.killeffects.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import gg.tropic.game.extensions.cosmetics.utilities.DeathAnimatorUtilities
import me.lucko.helper.Schedulers
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 12/22/2023
 */
@Service
object BloodKillEffect : KillEffect
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillEffectCosmeticCategory,
            this
        )
    }

    private val yLocations = doubleArrayOf(1.0, 1.25, 1.5, 1.75, 2.0)
    private fun playBloodEffect(
        player: Player, location: Location, yLevelAddition: Double
    )
    {
        player.playEffect(
            location.clone().add(0.0, yLevelAddition, 0.0),
            Effect.STEP_SOUND, Material.REDSTONE_BLOCK
        )
    }

    override fun applyTo(viewers: List<Player>, killer: Player, target: Player)
    {
        var ticks = 0

        Schedulers
            .sync()
            .runRepeating({ task ->
                if (ticks >= 5)
                {
                    viewers.forEach { viewer ->
                        viewer.hidePlayer(target)
                    }

                    task.closeAndReportException()
                    return@runRepeating
                }

                for (yAddition in yLocations)
                {
                    target.world.playEffect(
                        target.location.clone().add(0.0, yAddition, 0.0),
                        Effect.STEP_SOUND,
                        Material.REDSTONE_BLOCK
                    )

                    viewers.forEach { viewer ->
                        playBloodEffect(viewer, target.location, yAddition)
                    }
                }

                ticks += 1
            }, 0L, 10L)
    }

    override val resource = ImmutableResource("killeffects", "blood")
    override val name = "Blood"
    override val description = listOf(
        "Make blood shoot out of your",
        "opponent."
    )
    override val icon = ItemBuilder.of(Material.REDSTONE)
    override val price = 750.0f
}
