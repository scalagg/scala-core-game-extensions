package gg.scala.game.extensions.leveling

import gg.scala.commons.persist.datasync.DataSyncService
import gg.scala.commons.persist.datasync.DataSyncSource
import gg.scala.flavor.service.Service

/**
 * DataSync service for persisting and syncing level system configurations
 * across all servers via MongoDB + Redis.
 *
 * @author Limo
 * @date 2/11/2026
 */
@Service
object LevelSystemSyncService : DataSyncService<LevelSystemContainer>()
{
    override fun locatedIn() = DataSyncSource.Mongo

    override fun keys() = LevelSystemSyncKeys
    override fun type() = LevelSystemContainer::class.java

    /**
     * Retrieves a level system by its ID.
     */
    fun byId(id: String): LevelSystem?
    {
        return cached().levelSystems[id]
    }

    /**
     * Gets all registered level systems.
     */
    fun all(): Map<String, LevelSystem>
    {
        return cached().levelSystems
    }

    /**
     * Creates a new level system and persists it.
     */
    fun create(system: LevelSystem)
    {
        val container = cached()
        container.levelSystems[system.id] = system
        sync(container)
    }

    /**
     * Deletes a level system by ID and persists the change.
     */
    fun delete(id: String)
    {
        val container = cached()
        container.levelSystems.remove(id)
        sync(container)
    }

    /**
     * Updates an existing level system and persists.
     */
    fun update(system: LevelSystem)
    {
        val container = cached()
        container.levelSystems[system.id] = system
        sync(container)
    }
}
