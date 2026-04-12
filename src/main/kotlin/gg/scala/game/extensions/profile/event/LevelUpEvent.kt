package gg.scala.game.extensions.profile.event

import gg.scala.commons.event.StatelessEvent
import gg.scala.game.extensions.profile.CorePlayerProfile

class LevelUpEvent(
    val profile: CorePlayerProfile,
    val from: Int,
    val to: Int,
    val levelSystemId: String? = null
) : StatelessEvent()
