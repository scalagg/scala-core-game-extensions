package gg.tropic.game.extensions.cosmetics

import gg.tropic.game.extensions.profile.profile
import org.bukkit.entity.Player
import kotlin.math.roundToInt

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
interface PurchasableCosmetic : CosmeticLike
{
    val price: Float

    override fun canEquip(player: Player) =
        super.canEquip(player) && isOwnedBy(player)

    fun discountPrice(): Float?
    {
        return null
    }

    fun truePrice() = discountPrice() ?: price

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
