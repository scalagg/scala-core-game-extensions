package gg.scala.game.extensions.cosmetics.killeffects.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import kotlin.random.Random

/**
 * @author GrowlyX
 * @since 10/28/2023
 */
@Service
object LightningKillEffect : KillEffect
{
    override fun applyTo(viewers: List<Player>, killer: Player, target: Player)
    {

        viewers.forEach {
            spawnLightning(it, target.location)
        }

        Tasks.delayed(15L) {
            viewers.forEach { viewer ->
                viewer.hidePlayer(target)
            }
        }
    }

    private fun spawnLightning(player: Player, location: Location)
    {
        player.world.strikeLightningEffect(location)
        val thunderSoundPitch = 0.8f + Random.nextFloat() * 0.2f
        val explodeSoundPitch = 0.5f + Random.nextFloat() * 0.2f

        player.playSound(location, Sound.AMBIENCE_THUNDER, 10000.0f, thunderSoundPitch)
        player.playSound(location, Sound.EXPLODE, 2.0f, explodeSoundPitch)
    }

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillEffectCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("killeffects", "lightning")
    override val name = "Lightning"
    override val description = listOf(
        "Zap your Opponent and watch",
        "them get sent to the heavens!"
    )
    override val icon = ItemBuilder.of(XMaterial.BLAZE_ROD)
}
