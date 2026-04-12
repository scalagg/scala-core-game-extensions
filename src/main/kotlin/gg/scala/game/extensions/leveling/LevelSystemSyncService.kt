package gg.scala.game.extensions.leveling

import gg.scala.commons.persist.datasync.DataSyncService
import gg.scala.commons.persist.datasync.DataSyncSource
import gg.scala.flavor.service.Service

/**
 * @author Limo
 * @date 2/11/2026
 */
@Service
object LevelSystemSyncService : DataSyncService<LevelSystemContainer>()
{
    override fun locatedIn() = DataSyncSource.Mongo

    override fun keys() = LevelSystemSyncKeys
    override fun type() = LevelSystemContainer::class.java

    fun byId(id: String): LevelSystem?
    {
        return cached().levelSystems[id]
    }

    fun all(): Map<String, LevelSystem>
    {
        return cached().levelSystems
    }

    fun create(system: LevelSystem)
    {
        val container = cached()
        container.levelSystems[system.id] = system
        sync(container)
    }

    fun delete(id: String)
    {
        val container = cached()
        container.levelSystems.remove(id)
        sync(container)
    }

    fun update(system: LevelSystem)
    {
        val container = cached()
        container.levelSystems[system.id] = system
        sync(container)
    }
}
