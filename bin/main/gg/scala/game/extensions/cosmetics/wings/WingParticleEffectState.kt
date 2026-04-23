package gg.scala.game.extensions.cosmetics.wings

import gg.scala.game.extensions.cosmetics.CosmeticTickState

/**
 * @author GrowlyX
 * @since 12/24/2023
 */
class WingParticleEffectState(
    var lastMovement: Long = System.currentTimeMillis()
) : CosmeticTickState
