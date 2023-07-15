package gg.tropic.game.extensions.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
@Service
object EquipOnLoginCosmeticService
{
    @Configure
    fun configure()
    {
        Events
            .subscribe(PlayerJoinEvent::class.java)
            .handler {
                CosmeticRegistry
                    .allEquipOnLoginCosmetics()
                    .forEach { cosmetic ->
                        cosmetic as CosmeticLike
                        if (
                            cosmetic.shouldEquipOnLogin(it.player) &&
                            cosmetic.equipped(it.player)
                        )
                        {
                            cosmetic.equip(it.player)
                            println("equiping")
                        }
                    }
            }
    }
}
