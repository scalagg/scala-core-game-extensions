package gg.scala.game.extensions.music

import net.evilblock.cubed.util.nms.MinecraftProtocol
import net.evilblock.cubed.util.Reflection
import org.bukkit.Location
import org.bukkit.entity.Player

enum class Music(
    val id: String,
    val lengthSeconds: Int
)
{
    Eleven("11", 60 + 11),
    Thirteen("13", 178),
    Blocks("blocks", 345),
    Chirp("chirp", 185),
    Far("far", 174),
    Mall("mall", 180 + 17),
    Mellohi("mellohi", 60 + 36),
    Cat("cat", 185),
    Stal("stal", 120 + 30),
    Strad("strad", 188),
    Wait("wait", 3 * 60 + 58),
    Ward("ward", 4 * 60 + 11);

    fun playFor(player: Player)
    {
        val soundKey = "${if (MinecraftProtocol.getPlayerVersion(player) >= 393) "music_disc" else "records"}.$id"
        val stringMethod = Reflection.getMethodSuppressed(
            player.javaClass,
            "playSound",
            Location::class.java,
            String::class.java,
            Float::class.javaPrimitiveType!!,
            Float::class.javaPrimitiveType!!
        )

        if (stringMethod != null) {
            stringMethod.invoke(player, player.location, soundKey, 1000.0f, 1.0f)
        }
    }
}
