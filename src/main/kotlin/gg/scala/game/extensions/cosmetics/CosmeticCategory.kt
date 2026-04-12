package gg.scala.game.extensions.cosmetics

import org.bukkit.entity.Player

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
