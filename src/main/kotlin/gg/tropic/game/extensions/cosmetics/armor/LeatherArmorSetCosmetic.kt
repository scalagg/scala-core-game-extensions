package gg.tropic.game.extensions.cosmetics.armor

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.*
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.menus.SelectColorMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ColorUtil
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
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
object LeatherArmorSetCosmetic : PurchasableCosmetic, EquipOnLogin
{
    data class LeatherArmorConfig(
        var r: Int = 0,
        var g: Int = 0,
        var b: Int = 0
    ) : DynamicCosmeticConfigStore
    {
        override fun getAbstractType() =
            LeatherArmorConfig::class.java
    }

    object DynamicLeatherArmorCosmetic : DynamicCosmetic<LeatherArmorConfig>(LeatherArmorSetCosmetic)
    {
        override fun equip(player: Player, configuration: LeatherArmorConfig)
        {
            val color = Color.fromRGB(
                configuration.r,
                configuration.g,
                configuration.b
            )

            fun ItemStack.colorLeatherArmor(color: Color): ItemStack {
                val meta = itemMeta as LeatherArmorMeta
                meta.color = color
                itemMeta = meta
                return this
            }

            player.inventory.chestplate = ItemBuilder(Material.LEATHER_CHESTPLATE)
                .build()
                .colorLeatherArmor(color)

            player.inventory.leggings = ItemBuilder(Material.LEATHER_LEGGINGS)
                .build()
                .colorLeatherArmor(color)

            player.inventory.helmet = ItemBuilder(Material.LEATHER_HELMET)
                .build()
                .colorLeatherArmor(color)

            player.inventory.boots = ItemBuilder(Material.LEATHER_BOOTS)
                .build()
                .colorLeatherArmor(color)

            player.updateInventory()
        }

        override fun openEditor(player: Player)
        {
            object : DynamicCosmeticConfigEditorMenu<LeatherArmorConfig>(
                ArmorCosmeticCategory,
                this@DynamicLeatherArmorCosmetic,
                serveConfiguration(player)
            )
            {
                override fun populate(player: Player, buttons: MutableMap<Int, Button>)
                {
                    buttons.putAll(
                        SelectColorMenu {
                            configuration.r = it.red
                            configuration.g = it.green
                            configuration.b = it.blue

                            player.sendMessage(
                                "${CC.GREEN}You selected: ${ColorUtil.toChatColor(it)}${ColorUtil.getColorName(it)}"
                            )
                        }.getButtons(player)
                    )
                }
            }.openMenu(player)
        }

        override fun buildDefaultConfiguration() = LeatherArmorConfig()
    }

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ArmorCosmeticCategory,
            DynamicLeatherArmorCosmetic
        )
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
