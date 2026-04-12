package gg.scala.game.extensions.economy

import gg.scala.commons.persist.datasync.DataSyncKeys

/**
 * @author Limo
 * @date 2/11/2026
 */
object EconomySyncKeys : DataSyncKeys
{
    override fun store() = keyOf("economy", "configuration")
    override fun newStore() = "economy:store"

    override fun sync() = keyOf("economy", "sync")
}
