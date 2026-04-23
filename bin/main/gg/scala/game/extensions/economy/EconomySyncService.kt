package gg.scala.game.extensions.economy

import gg.scala.commons.persist.datasync.DataSyncService
import gg.scala.commons.persist.datasync.DataSyncSource
import gg.scala.flavor.service.Service

/**
 * DataSync service for persisting and syncing economy configurations
 * (currency definitions) across all servers via MongoDB + Redis.
 *
 * @author Limo
 * @date 2/11/2026
 */
@Service
object EconomySyncService : DataSyncService<EconomyDataCache>()
{
    override fun locatedIn() = DataSyncSource.Mongo

    override fun keys() = EconomySyncKeys
    override fun type() = EconomyDataCache::class.java

    /**
     * Retrieves a currency by its ID.
     */
    fun currencyById(id: String): Currency?
    {
        return cached().economies[id]?.currency
    }

    /**
     * Retrieves an economy data entry by ID.
     */
    fun economyById(id: String): EconomyData?
    {
        return cached().economies[id]
    }

    /**
     * Gets all registered economies.
     */
    fun all(): Map<String, EconomyData>
    {
        return cached().economies
    }

    /**
     * Creates or updates a currency and persists it.
     */
    fun createOrUpdate(economy: EconomyData)
    {
        val container = cached()
        container.economies[economy.id] = economy
        sync(container)
    }

    /**
     * Deletes an economy by ID and persists the change.
     */
    fun delete(id: String)
    {
        val container = cached()
        container.economies.remove(id)
        sync(container)
    }

    override fun postReload()
    {
        // Keep the legacy EconomyDataSync in sync
        EconomyDataSync.reload(cached())
    }
}
