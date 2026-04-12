package gg.scala.game.extensions.economy

import gg.scala.commons.persist.datasync.DataSyncService
import gg.scala.commons.persist.datasync.DataSyncSource
import gg.scala.flavor.service.Service

/**
 * @author Limo
 * @date 2/11/2026
 */
@Service
object EconomySyncService : DataSyncService<EconomyDataCache>()
{
    override fun locatedIn() = DataSyncSource.Mongo

    override fun keys() = EconomySyncKeys
    override fun type() = EconomyDataCache::class.java

    fun currencyById(id: String): Currency?
    {
        return cached().economies[id]?.currency
    }

    fun economyById(id: String): EconomyData?
    {
        return cached().economies[id]
    }

    fun all(): Map<String, EconomyData>
    {
        return cached().economies
    }

    fun createOrUpdate(economy: EconomyData)
    {
        val container = cached()
        container.economies[economy.id] = economy
        sync(container)
    }

    fun delete(id: String)
    {
        val container = cached()
        container.economies.remove(id)
        sync(container)
    }

    override fun postReload()
    {

        EconomyDataSync.reload(cached())
    }
}
