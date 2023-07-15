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

    fun canPurchase(player: Player): Boolean
    {
        return player.profile.coins >= price
    }

    fun isOwnedBy(player: Player) = player.profile
        .ownedCosmetics.any {
            it.grantsAccessTo(resource)
        }

    fun purchase(player: Player)
    {
        with(player.profile) {
            removeCoins(price.roundToInt())
            ownedCosmetics += resource
            save()
        }
    }
}
