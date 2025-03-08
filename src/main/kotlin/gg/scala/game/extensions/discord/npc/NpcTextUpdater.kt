package gg.scala.game.extensions.discord.npc

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.discord.DiscordService
import me.lucko.helper.Schedulers
import net.evilblock.cubed.entity.EntityHandler
import net.evilblock.cubed.entity.npc.NpcEntity

/**
 * @author Topu
 * @date 3/8/2025
 */
@Service
object NpcTextUpdater : Runnable
{
    @Configure
    fun configure()
    {
        Schedulers.async().runRepeating(
            this, 0L, 10L
        )
    }

    fun getNpcEntities(): List<NpcEntity> =
        EntityHandler.getEntitiesByType(NpcEntity::class.java)

    override fun run()
    {
        getNpcEntities()
            .forEach {
                if (it.condition != "discord")
                {
                    return@forEach
                }
                it.hologram.updateLines(
                    handle(it.getLines())
                )
            }
    }

    fun handle(list: List<String>): MutableList<String>
    {
        val replaced =
            mutableListOf<String>()

        for (message in list)
        {
            replaced.add(
                message.replace(
                    "%discord-memberCount%",
                    DiscordService.fetchOnlineDiscordMembers()
                )
            )
        }

        return replaced
    }
}