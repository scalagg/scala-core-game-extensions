package gg.tropic.game.extensions.cosmetics.killeffects.cosmetics

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import net.evilblock.cubed.util.bukkit.ItemBuilder
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
    }

    private fun spawnLightning(player: Player, location: Location)
    {
        val lightningPacket = PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_WEATHER)
        lightningPacket.modifier.writeDefaults()
        lightningPacket.integers.write(0, 128)
        lightningPacket.integers.write(4, 1)
        lightningPacket.integers.write(1, (location.x * 32.0).toInt())
        lightningPacket.integers.write(2, (location.y * 32.0).toInt())
        lightningPacket.integers.write(3, (location.z * 32.0).toInt())

        ProtocolLibrary.getProtocolManager().sendServerPacket(player, lightningPacket)
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
        "Become Zeus! Vaporize your opponent",
        "with your lightning powers!"
    )
    override val icon = ItemBuilder.of(Material.BLAZE_ROD)
    override val price = 250.0f
}
