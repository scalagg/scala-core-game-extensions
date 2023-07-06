package gg.tropic.game.extensions.profile.event

import gg.scala.commons.event.StatelessEvent
import gg.tropic.game.extensions.profile.CorePlayerProfile

/**
 * @author GrowlyX
 * @since 7/5/2023
 */
class LevelUpEvent(
    val profile: CorePlayerProfile,
    val from: Int,
    val to: Int
) : StatelessEvent()
