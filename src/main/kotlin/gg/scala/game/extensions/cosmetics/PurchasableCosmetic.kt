package gg.scala.game.extensions.cosmetics

import gg.scala.game.extensions.cosmetics.discounts.DiscountSyncService
import gg.scala.game.extensions.profile.profile
import org.bukkit.entity.Player
import kotlin.math.roundToInt

interface PurchasableCosmetic : CosmeticLike
{
    val price: Float

    override fun canEquip(player: Player) =
        super.canEquip(player) && isOwnedBy(player)

    fun purchasable() = true

    fun discount() = DiscountSyncService.byResource(resource)
    fun discountPrice() = discount()?.let { price - ((it.percentage.toDouble() / 100.0) * price) }

    fun truePrice() = discountPrice()?.toFloat() ?: price

    fun canPurchase(player: Player): Boolean
    {
        return player.profile.coins >= truePrice()
    }

    fun isOwnedBy(player: Player) = player.hasPermission("core.cosmetics.overrides.$resource") ||
        player.hasPermission("core.cosmetics.overrides.${resource.namespace}") ||
        player.profile
            .ownedCosmetics.any {
                it.grantsAccessTo(resource)
            }

    fun purchase(player: Player)
    {
        with(player.profile) {
            removeCoins(truePrice().roundToInt())
            ownedCosmetics += resource
            save()
        }
    }
}
