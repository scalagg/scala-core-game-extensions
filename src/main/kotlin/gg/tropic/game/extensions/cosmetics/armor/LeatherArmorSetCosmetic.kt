package gg.tropic.game.extensions.cosmetics.armor

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.*
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ColorUtil
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
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
object LeatherArmorSetCosmetic : PurchasableCosmetic, DynamicCosmetic<LeatherArmorSetCosmetic.LeatherArmorConfig>
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

    override fun equip(player: Player, configuration: LeatherArmorConfig)
    {
        val color = Color.fromRGB(
            configuration.r,
            configuration.g,
            configuration.b
        )

        fun colorLeatherArmor(itemStack: ItemStack, color: Color): ItemStack {
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

        player.inventory.helmet = colorLeatherArmor(
            ItemStack(Material.LEATHER_HELMET), color
        )

        player.inventory.boots = colorLeatherArmor(
            ItemStack(Material.LEATHER_BOOTS), color
        )

        player.updateInventory()
    }

    override fun openEditor(player: Player)
    {
        object : DynamicCosmeticConfigEditorMenu<LeatherArmorConfig>(
            ArmorCosmeticCategory,
            this@LeatherArmorSetCosmetic,
            serveConfiguration(player)
        )
        {
            init
            {
                autoUpdate = true
            }

            override fun getGlobalButtons(player: Player): Map<Int, Button>
            {
                val buttons = mutableMapOf<Int, Button>()
                buttons[4] = ItemBuilder
                    .of(
                        XMaterial.WHITE_WOOL
                    )
                    .name("${CC.WHITE}Custom color")
                    .addToLore(
                        "${CC.GRAY}Enter your own hex color",
                        "${CC.GRAY}to use as your armor color!",
                        "",
                        "${CC.GREEN}Click to enter custom color!"
                    )
                    .toButton { _, _ ->
                        if (!player.hasPermission("core.cosmetics.armor.color.custom"))
                        {
                            player.sendMessage("${CC.RED}You do not have permission to use this feature! Purchase ${CC.GOLD}Pro${CC.RED} rank to gain access.")
                            return@toButton
                        }

                        player.closeInventory()
                        InputPrompt()
                            .withText(
                                "${CC.GREEN}Enter a hex code ${CC.GRAY}(example: #FFFFF)${CC.GREEN}:"
                            )
                            .acceptInput { _, hex ->
                                with(java.awt.Color.decode(hex)) {
                                    configuration.r = red
                                    configuration.g = green
                                    configuration.b = blue

                                    player.sendMessage(
                                        "${CC.GREEN}You selected a custom color!"
                                    )
                                    openMenu(player)
                                }
                            }
                            .start(player)
                    }

                return buttons
            }

            override fun populate(player: Player, buttons: MutableMap<Int, Button>)
            {
                ColorUtil.getPredefinedColors()
                    .forEach {
                        buttons[buttons.size] = ItemBuilder
                            .of(
                                Material.WOOL
                            )
                            .data(
                                ColorUtil.toWoolData(
                                    ColorUtil.toChatColor(it)
                                ).toShort()
                            )
                            .name(
                                "${ColorUtil.toChatColor(it)}${
                                    ColorUtil.getColorName(it)
                                        .lowercase()
                                        .capitalize()
                                        .replace("_", " ")
                                }"
                            )
                            .toButton { _, _ ->
                                configuration.r = it.red
                                configuration.g = it.green
                                configuration.b = it.blue

                                player.sendMessage(
                                    "${CC.GREEN}You selected: ${ColorUtil.toChatColor(it)}${ColorUtil.getColorName(it)}"
                                )
                            }
                    }
            }
        }.openMenu(player)
    }

    override fun buildDefaultConfiguration() = LeatherArmorConfig()

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ArmorCosmeticCategory,
            LeatherArmorSetCosmetic
        )
    }

    override val price = 1000.0f

    override fun unequip(player: Player)
    {
        player.inventory.chestplate = null
        player.inventory.leggings = null
        player.inventory.helmet = null
        player.inventory.boots = null
        player.updateInventory()
    }

    override fun equip(player: Player)
    {
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
