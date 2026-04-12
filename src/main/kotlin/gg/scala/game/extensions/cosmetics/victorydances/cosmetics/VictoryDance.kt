package gg.scala.game.extensions.cosmetics.victorydances.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.game.extensions.cosmetics.*
import gg.scala.game.extensions.cosmetics.victorydances.VictoryDanceCosmeticCategory
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.entity.Player

/**
 * @date 2/26/2025
 */
interface VictoryDance :
    CosmeticLike,
    DynamicCosmetic<VictoryDance.VictoryDanceConfig>,
    MultiEquipCosmetic
{

    data class VictoryDanceConfig(
        var flight: Boolean? = false,
        var clearInventory: Boolean? = false
    ) : DynamicCosmeticConfigStore
    {
        override fun getAbstractType() =
            VictoryDanceConfig::class.java
    }

    override fun unequip(player: Player)
    {
    }

    override fun equip(player: Player, configuration: VictoryDanceConfig)
    {
    }

    override fun openEditor(player: Player)
    {
        object : DynamicCosmeticConfigEditorMenu<VictoryDanceConfig>(
            VictoryDanceCosmeticCategory,
            this@VictoryDance,
            serveConfiguration(player)
        )
        {
            init
            {
                updateAfterClick = true
            }

            override fun populate(player: Player, buttons: MutableMap<Int, Button>)
            {
                buttons[0] = ItemBuilder
                    .of(XMaterial.FEATHER)
                    .name(
                        "${CC.YELLOW}Flight: ${
                            if (configuration.flight != false) "${CC.GREEN}Enabled" else "${CC.RED}Disabled"
                        }"
                    )
                    .toButton { _, _ ->
                        configuration.flight = !(configuration.flight ?: true)
                        Button.playNeutral(player)
                    }

                buttons[1] = ItemBuilder
                    .of(XMaterial.CHEST)
                    .name(
                        "${CC.YELLOW}Clear Inventory: ${
                            if (configuration.clearInventory != false) "${CC.GREEN}Enabled" else "${CC.RED}Disabled"
                        }"
                    )
                    .toButton { _, _ ->
                        configuration.clearInventory = !(configuration.clearInventory ?: true)
                        Button.playNeutral(player)
                    }
            }
        }.openMenu(player)
    }

    override fun buildDefaultConfiguration() = VictoryDanceConfig()

    fun applyTo(viewers: List<Player>, killer: Player, target: Player?)
}
