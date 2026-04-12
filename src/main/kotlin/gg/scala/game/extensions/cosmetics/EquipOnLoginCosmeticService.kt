package gg.scala.game.extensions.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

@Service
object EquipOnLoginCosmeticService
{
    fun callOnLoginFor(player: Player) = CosmeticRegistry
        .allEquipOnLoginCosmetics()
        .forEach { cosmetic ->
            cosmetic as CosmeticLike
            if (
                cosmetic.shouldEquipOnLogin(player) &&
                cosmetic.equipped(player) &&
                CosmeticLocalConfig.enableCosmeticResources
            )
            {
                cosmetic.equip(player)
            }
        }

    var defaultFunctionality = true

    @Configure
    fun configure()
    {
        Events
            .subscribe(
                PlayerJoinEvent::class.java,
                EventPriority.LOWEST
            )
            .filter { defaultFunctionality }
            .handler {
                callOnLoginFor(it.player)
            }
    }
}
