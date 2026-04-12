package gg.scala.game.extensions.leveling

import gg.scala.commons.persist.datasync.DataSyncKeys

/**
 * @author Limo
 * @date 2/11/2026
 */
object LevelSystemSyncKeys : DataSyncKeys
{
    override fun store() = keyOf("leveling", "configuration")
    override fun newStore() = "leveling:store"

    override fun sync() = keyOf("leveling", "sync")
}
