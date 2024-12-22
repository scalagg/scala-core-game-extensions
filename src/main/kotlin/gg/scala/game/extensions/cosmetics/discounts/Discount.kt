package gg.scala.game.extensions.cosmetics.discounts

import gg.scala.game.extensions.cosmetics.ImmutableResource

/**
 * @author GrowlyX
 * @since 8/11/2024
 */
data class Discount(
    val resource: ImmutableResource,
    var percentage: Int,
    var expirationDate: Long? = null,
    var active: Boolean = false
)
