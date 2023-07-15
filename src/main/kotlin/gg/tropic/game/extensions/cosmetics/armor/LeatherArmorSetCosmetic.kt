package gg.tropic.game.extensions.cosmetics.armor

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.EquipOnLogin
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.PurchasableCosmetic
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
@Service
object LeatherArmorSetCosmetic : PurchasableCosmetic, EquipOnLogin
{
    @Configure
    fun configure()
    {
        CosmeticRegistry.register(ArmorCosmeticCategory, this)
    }

    override val price = 1000.0f

    override fun unequip(player: Player)
    {
        Tasks.delayed(1L) {
            player.inventory.chestplate = null
            player.inventory.leggings = null
            player.inventory.helmet = null
            player.inventory.boots = null
            player.updateInventory()
        }
    }

    override fun equip(player: Player)
    {
        Tasks.delayed(1L) {
            player.inventory.chestplate = ItemStack(Material.LEATHER_CHESTPLATE)
            player.inventory.leggings = ItemStack(Material.LEATHER_LEGGINGS)
            player.inventory.helmet = ItemStack(Material.LEATHER_HELMET)
            player.inventory.boots = ItemStack(Material.LEATHER_BOOTS)
            player.updateInventory()
        }
    }

    override val resource =
        ImmutableResource("armor", "leather")

    override val name = "Leather"
    override val description = listOf(
        "Equips a set of leather armor!"
    )
    override val icon = ItemBuilder
        .of(Material.LEATHER_CHESTPLATE)
}
