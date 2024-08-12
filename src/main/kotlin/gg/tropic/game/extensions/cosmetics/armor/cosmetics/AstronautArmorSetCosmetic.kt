package gg.tropic.game.extensions.cosmetics.armor.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.*
import gg.tropic.game.extensions.cosmetics.armor.ArmorCosmeticCategory
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
@Service
object AstronautArmorSetCosmetic :
    PurchasableCosmetic,
    NewlyReleased,
    EquipOnLogin
{
    override fun equip(player: Player)
    {
        val color = Color.AQUA
        fun colorLeatherArmor(itemStack: ItemStack, color: Color): ItemStack
        {
            val meta = itemStack.itemMeta as LeatherArmorMeta
            meta.color = color
            itemStack.itemMeta = meta
            return itemStack
        }

        player.inventory.chestplate = colorLeatherArmor(
            ItemStack(Material.LEATHER_CHESTPLATE), color
        )

        player.inventory.leggings = colorLeatherArmor(
            ItemStack(Material.LEATHER_LEGGINGS), color
        )

        player.inventory.helmet = ItemBuilder
            .of(XMaterial.LIGHT_BLUE_STAINED_GLASS)
            .build()

        player.inventory.boots = colorLeatherArmor(
            ItemStack(Material.LEATHER_BOOTS), color
        )

        player.updateInventory()
    }


    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ArmorCosmeticCategory,
            AstronautArmorSetCosmetic
        )
    }

    override val price = 1500.0f

    override fun unequip(player: Player)
    {
        player.inventory.chestplate = null
        player.inventory.leggings = null
        player.inventory.helmet = null
        player.inventory.boots = null
        player.updateInventory()
    }

    override val resource =
        ImmutableResource("armor", "astronaut")

    override val name = "Astronaut Suit"
    override val description = listOf(
        "Pretend to be in space!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.LIGHT_BLUE_STAINED_GLASS)
        .glow()
}
