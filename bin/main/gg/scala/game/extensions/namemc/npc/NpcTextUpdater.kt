package gg.scala.game.extensions.namemc.npc

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.namemc.NameMCService
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
                if (it.condition != "namemc")
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
                    "%namemc-likes%",
                    NameMCService.fetchLikes()
                )
            )
        }

        return replaced
    }
}
