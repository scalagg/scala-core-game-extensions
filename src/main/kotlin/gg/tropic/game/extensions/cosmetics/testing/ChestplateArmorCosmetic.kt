package gg.tropic.game.extensions.cosmetics.testing

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.PurchasableCosmetic
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
@Service
object ChestplateArmorCosmetic : PurchasableCosmetic
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(ArmorCosmeticCategory, this)
    }

    override val price = 20.0f

    override fun unequip(player: Player)
    {
        player.inventory.chestplate = null
        player.updateInventory()
    }

    override fun equip(player: Player)
    {
        player.inventory.chestplate = icon.build()
        player.updateInventory()
    }

    override val resource = ImmutableResource("armor", "chestplate")
    override val name = "Chestplate"
    override val description = listOf(
        "Chestie!"
    )
    override val icon = ItemBuilder
        .of(Material.LEATHER_CHESTPLATE)
}
