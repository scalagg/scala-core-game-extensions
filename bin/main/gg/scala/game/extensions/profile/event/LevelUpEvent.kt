package gg.scala.game.extensions.profile.event

import gg.scala.commons.event.StatelessEvent
import gg.scala.game.extensions.profile.CorePlayerProfile

/**
 * Fired when a player levels up in any level system.
 *
 * @author GrowlyX
 * @since 7/5/2023
 */
class LevelUpEvent(
    val profile: CorePlayerProfile,
    val from: Int,
    val to: Int,
    val levelSystemId: String? = null
) : StatelessEvent()
