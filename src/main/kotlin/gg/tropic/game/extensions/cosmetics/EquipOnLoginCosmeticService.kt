package gg.tropic.game.extensions.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
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

    @Configure
    fun configure()
    {
        Events
            .subscribe(
                PlayerJoinEvent::class.java,
                EventPriority.LOWEST
            )
            .handler {
                callOnLoginFor(it.player)
            }
    }
}
