package gg.tropic.game.extensions.profile

import gg.scala.commons.issuer.ScalaPlayer
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 6/12/2023
 */
val Player.profile: CorePlayerProfile
    get() = CorePlayerProfileService.find(this)!!

val ScalaPlayer.profile: CorePlayerProfile
    get() = CorePlayerProfileService.find(bukkit())!!
