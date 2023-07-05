package gg.tropic.game.extensions.profile

import gg.scala.store.storage.storable.IDataStoreObject
import java.util.*

/**
 * @author GrowlyX
 * @since 7/5/2023
 */
data class CorePlayerProfile(
    override val identifier: UUID,
) : IDataStoreObject
{
    // TODO: use algorithm for initial
    var currentLevel: Int = 1
        private set(value)
        {

        }
}
