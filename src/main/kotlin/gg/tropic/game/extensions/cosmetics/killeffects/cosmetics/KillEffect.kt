package gg.tropic.game.extensions.cosmetics.killeffects.cosmetics

import gg.tropic.game.extensions.cosmetics.*
import gg.tropic.game.extensions.cosmetics.killeffects.KillEffectCosmeticCategory
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
    PurchasableCosmetic,
    DynamicCosmetic<KillEffect.KillEffectConfig>
{

    data class KillEffectConfig(
        var flightEnabled: Boolean = true
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
                    .of(Material.FEATHER)
                    .name("${CC.YELLOW}Flight: ${
                        if (configuration.flightEnabled) "${CC.GREEN}Enabled" else "${CC.RED}Disabled"
                    }")
                    .toButton { _, _ ->
                        configuration.flightEnabled = !configuration.flightEnabled
                        Button.playNeutral(player)
                    }
            }
        }.openMenu(player)
    }

    override fun buildDefaultConfiguration() = KillEffectConfig()

    fun applyTo(viewers: List<Player>, target: Player)
}
