package gg.scala.game.extensions.cosmetics.discounts

import gg.scala.commons.persist.datasync.DataSyncKeys

object DiscountSyncKeys : DataSyncKeys
{
    override fun store() = keyOf("asd", "configuration")
    override fun newStore() = "discount:store"

    override fun sync() = keyOf("discount", "sync")
}
