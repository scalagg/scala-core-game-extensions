package gg.scala.game.extensions.music

import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.entity.Player

class Playlist(
    private val musicDiscs: List<Music>,
    private var currentIndex: Int = 0,
    private var loop: Boolean = true
)
{
    fun play(player: Player)
    {
        val disc = musicDiscs
            .getOrNull(currentIndex++)
            ?: run {
                if (loop)
                {
                    currentIndex = 0
                    return@run musicDiscs[0]
                }

                return
            }

        if (player.isOnline)
        {
            disc.playFor(player)
            player.sendMessage("${CC.GRAY}(now playing: ${disc.id}!)")

            Tasks.delayed(
                disc.lengthSeconds * 20L
            ) {
                play(player)
            }
        }
    }
}
