package gg.tropic.game.extensions.cosmetics.discounts

import gg.scala.commons.persist.datasync.DataSyncService
import gg.scala.commons.persist.datasync.DataSyncSource
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.ImmutableResource

/**
 * @author GrowlyX
 * @since 10/16/2022
 */
@Service
object DiscountSyncService : DataSyncService<DiscountContainer>()
{
    override fun locatedIn() = DataSyncSource.Mongo

    fun byResource(resource: ImmutableResource) = cached()
        .discounts.values
        .firstOrNull {
            it.active && (it.expirationDate == null || System.currentTimeMillis() < it.expirationDate!!)
                && it.resource.grantsAccessTo(resource)
        }

    override fun keys() = DiscountSyncKeys
    override fun type() = DiscountContainer::class.java
}
