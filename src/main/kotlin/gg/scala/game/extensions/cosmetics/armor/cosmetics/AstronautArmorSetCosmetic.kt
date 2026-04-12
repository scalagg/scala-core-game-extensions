package gg.scala.game.extensions.cosmetics.armor.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.*
import gg.scala.game.extensions.cosmetics.armor.ArmorCosmeticCategory
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

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
            ItemStack(XMaterial.LEATHER_CHESTPLATE.parseMaterial()), color
        )

        player.inventory.leggings = colorLeatherArmor(
            ItemStack(XMaterial.LEATHER_LEGGINGS.parseMaterial()), color
        )

        player.inventory.helmet = ItemBuilder
            .of(XMaterial.LIGHT_BLUE_STAINED_GLASS)
            .build()

        player.inventory.boots = colorLeatherArmor(
            ItemStack(XMaterial.LEATHER_BOOTS.parseMaterial()), color
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
