package gg.scala.game.extensions.namemc

import gg.scala.commons.persist.datasync.DataSyncKeys
import gg.scala.commons.persist.datasync.DataSyncService
import gg.scala.commons.persist.datasync.DataSyncSource
import gg.scala.flavor.service.Service
import net.kyori.adventure.key.Key

/**
 * @date 2/7/2025
 */
@Service
object NameMCDataSync : DataSyncService<NameMCConfig>()
{

    object NameMCKeys : DataSyncKeys
    {
        override fun store() = Key.key("global", "namemc")
        override fun sync() = Key.key("global", "namemcsync")
        override fun newStore() = "namemc"
    }

    override fun locatedIn() = DataSyncSource.Mongo

    override fun keys() = NameMCKeys
    override fun type() = NameMCConfig::class.java

    override fun postReload()
    {
    }
}