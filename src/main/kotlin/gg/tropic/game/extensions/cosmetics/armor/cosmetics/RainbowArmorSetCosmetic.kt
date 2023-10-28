package gg.tropic.game.extensions.cosmetics.armor.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.*
import gg.tropic.game.extensions.cosmetics.armor.ArmorCosmeticCategory
import gg.tropic.game.extensions.cosmetics.armor.cosmetics.rainbow.RainbowData
import gg.tropic.game.extensions.cosmetics.armor.cosmetics.rainbow.hue2rgb
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.ItemUtils.colorLeatherArmor
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
@Service
object RainbowArmorSetCosmetic :
    PurchasableCosmetic,
    EquipOnLogin,
    SessionScopeTickableCosmetic<RainbowData>
{
    override fun equip(player: Player)
    {
    }

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            ArmorCosmeticCategory,
            RainbowArmorSetCosmetic
        )
    }

    override val price = 10000.0f
    override fun tick(player: Player, state: RainbowData)
    {
        state.colorHsl += 0.005

        if (state.colorHsl > 1) {
            state.colorHsl = 0.0
        }

        state.color = hue2rgb(state.colorHsl)
        player.inventory.helmet =
            colorLeatherArmor(
                ItemBuilder.of(Material.LEATHER_HELMET).name(name).build(),
                state.color
            )
        player.inventory.chestplate =
            colorLeatherArmor(
                ItemBuilder.of(Material.LEATHER_CHESTPLATE).name(name).build(),
                state.color
            )
        player.inventory.leggings =
            colorLeatherArmor(
                ItemBuilder.of(Material.LEATHER_LEGGINGS).name(name).build(),
                state.color
            )
        player.inventory.boots =
            colorLeatherArmor(
                ItemBuilder.of(Material.LEATHER_BOOTS).name(name).build(),
                state.color
            )
    }

    override fun typeOf() = RainbowData::class

    override fun unequip(player: Player)
    {
        player.inventory.chestplate = null
        player.inventory.leggings = null
        player.inventory.helmet = null
        player.inventory.boots = null
        player.updateInventory()
    }

    override val resource =
        ImmutableResource("armor", "rainbow")

    override val name = "Rainbow Armor"
    override val description = listOf(
        "Equips a set of rainbow armor!"
    )
    override val icon = ItemBuilder
        .of(XMaterial.GREEN_WOOL)
}
