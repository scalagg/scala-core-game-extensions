package gg.tropic.game.extensions.cosmetics.armor.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.*
import gg.tropic.game.extensions.cosmetics.armor.ArmorCosmeticCategory
import net.evilblock.cubed.util.CC
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
object KingArmorSetCosmetic :
    PurchasableCosmetic,
    NewlyReleased,
    EquipOnLogin
{
    override fun equip(player: Player)
    {
        fun colorLeatherArmor(itemStack: ItemStack, color: Color): ItemStack
        {
            val meta = itemStack.itemMeta as LeatherArmorMeta
            meta.color = color
            itemStack.itemMeta = meta
            return itemStack
        }

        player.inventory.chestplate = colorLeatherArmor(
            ItemStack(Material.LEATHER_CHESTPLATE), Color.fromRGB(235, 53, 16)
        )

        player.inventory.leggings = colorLeatherArmor(
            ItemStack(Material.LEATHER_LEGGINGS), Color.fromRGB(250, 105, 75)
        )

        player.inventory.helmet = ItemBuilder
            .of(Material.SKULL_ITEM)
            .data(3)
            .name("${CC.YELLOW}King's Helmet")
            .owner("gaetan202")
            .build()

        player.inventory.boots = colorLeatherArmor(
            ItemStack(Material.LEATHER_BOOTS), Color.fromRGB(255, 228, 92)
        )

        player.updateInventory()
    }


    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ArmorCosmeticCategory,
            KingArmorSetCosmetic
        )
    }

    override val price = 2500.0f

    override fun unequip(player: Player)
    {
        player.inventory.chestplate = null
        player.inventory.leggings = null
        player.inventory.helmet = null
        player.inventory.boots = null
        player.updateInventory()
    }

    override val resource =
        ImmutableResource("armor", "king")

    override val name = "King Suit"
    override val description = listOf(
        "Become a king!"
    )
    override val icon = ItemBuilder
        .of(Material.GOLD_HELMET)
        .glow()
}
