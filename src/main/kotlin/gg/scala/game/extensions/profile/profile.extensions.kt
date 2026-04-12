package gg.scala.game.extensions.profile

import gg.scala.commons.issuer.ScalaPlayer
import org.bukkit.entity.Player

const val coinSymbol = "⛀"
const val levelSymbol = "✪"

val Player.profile: CorePlayerProfile
    get() = CorePlayerProfileService.find(this)!!

val ScalaPlayer.profile: CorePlayerProfile
    get() = CorePlayerProfileService.find(bukkit())!!
