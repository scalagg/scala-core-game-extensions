package gg.scala.game.extensions.cosmetics.killeffects.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.PurchasableCosmetic
import gg.scala.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import gg.scala.game.extensions.projectiletrails.InstantFirework
import me.lucko.helper.Schedulers
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect

/**
 * @date 8/13/2024
 */
@Service
object HeadRocketKillEffect : KillEffect, PurchasableCosmetic
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
        Tasks.delayed(5L) {
            viewers.forEach { viewer ->
                viewer.hidePlayer(target)
            }
        }

        val stand = target.world.spawnEntity(target.location, EntityType.ARMOR_STAND) as ArmorStand
        stand.setGravity(false)
        stand.isVisible = false
        stand.helmet = ItemBuilder.of(XMaterial.SKELETON_SKULL).data(3).owner(target.name).build()

        var times = 0

        viewers.forEach { viewer ->
            viewer.playSound(target.location, Sound.CHICKEN_EGG_POP, 1.0F, 1.0F)
        }

        Schedulers
            .sync()
            .runRepeating({task ->
                if (times >= 30 || stand.isDead) {
                    task.closeAndReportException()

                    val location = stand.location
                    stand.remove()

                    gg.scala.game.extensions.projectiletrails.InstantFirework(
                        FireworkEffect.builder()
                            .with(FireworkEffect.Type.BALL)
                            .withColor(Color.TEAL)
                            .withColor(Color.AQUA)
                            .withColor(Color.WHITE)
                            .withFade(Color.GRAY)
                            .trail(true)
                            .build(),
                        location
                    )

                    viewers.forEach { viewer ->
                        viewer.playSound(stand.location, Sound.EXPLODE, 1.0F, 1.0F)
                    }
                    return@runRepeating
                }
                val newLoc = stand.location.clone().add(0.0, 0.3, 0.0)
                newLoc.yaw += 20
                stand.teleport(newLoc)
                ParticleBuilder(ParticleEffect.FIREWORKS_SPARK, stand.location).display(viewers)

                viewers.forEach { viewer ->
                    viewer.playSound(stand.location, Sound.CHICKEN_EGG_POP, 1f, 1f)
                }
                times++
            }, 0L, 1L)
    }

    override val price = 1500f

    override val resource = ImmutableResource("Killeffects", "headRocket")
    override val name = "Head Rocket"
    override val description = listOf(
        "Launch your enemy head",
        "to the sky!"
    )
    override val icon = ItemBuilder.of(XMaterial.SKELETON_SKULL).data(3)
}
