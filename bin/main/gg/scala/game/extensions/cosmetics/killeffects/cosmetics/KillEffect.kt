package gg.scala.game.extensions.cosmetics.killeffects.cosmetics

import com.cryptomorin.xseries.XMaterial
import gg.scala.game.extensions.cosmetics.*
import gg.scala.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/28/2023
 */
interface KillEffect :
    CosmeticLike,
    DynamicCosmetic<KillEffect.KillEffectConfig>,
    MultiEquipCosmetic
{

    data class KillEffectConfig(
        var flight: Boolean? = false,
        var clearInventory: Boolean? = false
    ) : DynamicCosmeticConfigStore
    {
        override fun getAbstractType() =
            KillEffectConfig::class.java
    }

    override fun unequip(player: Player)
    {
    }

    override fun equip(player: Player, configuration: KillEffectConfig)
    {
    }

    override fun openEditor(player: Player)
    {
        object : DynamicCosmeticConfigEditorMenu<KillEffectConfig>(
            KillEffectCosmeticCategory,
            this@KillEffect,
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
                    .name("${CC.YELLOW}Flight: ${
                        if (configuration.flight != false) "${CC.GREEN}Enabled" else "${CC.RED}Disabled"
                    }")
                    .toButton { _, _ ->
                        configuration.flight = !(configuration.flight ?: true)
                        Button.playNeutral(player)
                    }

                buttons[1] = ItemBuilder
                    .of(XMaterial.CHEST)
                    .name("${CC.YELLOW}Clear Inventory: ${
                        if (configuration.clearInventory != false) "${CC.GREEN}Enabled" else "${CC.RED}Disabled"
                    }")
                    .toButton { _, _ ->
                        configuration.clearInventory = !(configuration.clearInventory ?: true)
                        Button.playNeutral(player)
                    }
            }
        }.openMenu(player)
    }

    override fun buildDefaultConfiguration() = KillEffectConfig()

    fun applyTo(viewers: List<Player>, killer: Player, target: Player)
}
