package gg.scala.game.extensions.cosmetics

import gg.scala.game.extensions.profile.profile
import org.bukkit.entity.Player

interface CosmeticLike : ResourceHolder, FrontendComponent
{
    fun unequip(player: Player)
    fun equip(player: Player)

    fun unequipInternal(player: Player)
    {
        unequip(player)
        with(player.profile) {
            setEquipped(player, false)
            save()
        }
    }

    fun equipInternal(player: Player)
    {
        equip(player)
        with(player.profile) {
            setEquipped(player, true)
            save()
        }
    }

    fun canEquip(player: Player) = player.profile
        .run {
            !equippedCosmetics.contains(resource)
        }

    fun equipped(player: Player) = player.profile
        .equippedCosmetics.contains(resource)

    fun setEquipped(player: Player, value: Boolean)
    {
        if (value)
        {
            player.profile.equippedCosmetics.add(resource)
        } else
        {
            player.profile.equippedCosmetics.remove(resource)
        }
    }
}
