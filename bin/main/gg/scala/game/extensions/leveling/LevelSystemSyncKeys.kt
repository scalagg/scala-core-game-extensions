package gg.scala.game.extensions.leveling

import gg.scala.commons.persist.datasync.DataSyncKeys

/**
 * DataSync keys for level system configuration persistence.
 *
 * @author Limo
 * @date 2/11/2026
 */
object LevelSystemSyncKeys : DataSyncKeys
{
    override fun store() = keyOf("leveling", "configuration")
    override fun newStore() = "leveling:store"

    override fun sync() = keyOf("leveling", "sync")
}
