package gg.scala.game.extensions.feature.event

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * @author Limo
 * @date 2/5/2026
 */
class LevelPrefixSelectEvent(
    val player: Player,
    var prefix: String = "",
    var gamemode: String? = "default",
    val level: Int = 0,
    val levelSystemId: String? = null
) : Event(false), Cancellable
{
    private var cancelled = false

    companion object
    {
        @JvmStatic
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList
    }

    override fun getHandlers() = handlerList
    override fun isCancelled() = cancelled
    override fun setCancelled(cancel: Boolean) { cancelled = cancel }
}
