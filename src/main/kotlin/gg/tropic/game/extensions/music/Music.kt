package gg.tropic.game.extensions.music

import com.cryptomorin.xseries.ReflectionUtils
import net.evilblock.cubed.util.nms.MinecraftProtocol
import net.evilblock.cubed.util.nms.MinecraftReflection
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect
import org.bukkit.entity.Player

/**
 * Simple utility to play a music disc for a
 *
 * @author GrowlyX
 * @since 7/24/2023
 */
enum class Music(val id: String)
{
    Eleven("11"),
    Thirteen("13"),
    Blocks("blocks"),
    Chirp("chirp"),
    Far("far"),
    Mall("mall"),
    Mellohi("mellohi"),
    Cat("cat"),
    Stal("stal"),
    Strad("strad"),
    Wait("wait"),
    Ward("ward");

    fun playFor(player: Player) = ReflectionUtils
        .sendPacket(
            player,
            PacketPlayOutNamedSoundEffect(
                "${
                    if (MinecraftProtocol.getPlayerVersion(player) >= 393) 
                        "music_disc" else "records"
                }.cat",
                player.location.x,
                player.location.y,
                player.location.z,
                1000.0f, 1.0f
            )
        )
}
