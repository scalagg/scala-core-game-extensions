package gg.tropic.game.extensions.cosmetics

import org.bukkit.entity.Player

/**
 * Cosmetic categories act as classifiers for Cosmetics. Cosmetic
 * categories hold unique namespaces, which are used when determining a
 * Player's access to a particular Cosmetic.
 *
 * @author GrowlyX
 * @since 7/14/2023
 */
interface CosmeticCategory : ResourceHolder, FrontendComponent
{
    fun cosmetics(): List<CosmeticLike>
    {
        return CosmeticRegistry.findRelatedTo(this)
    }

    fun ownedCosmetics(player: Player) = cosmetics()
        .filter {
            it !is PurchasableCosmetic || it.isOwnedBy(player)
        }
}
