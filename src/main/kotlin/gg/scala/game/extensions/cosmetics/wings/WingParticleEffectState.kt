package gg.scala.game.extensions.cosmetics.wings

import gg.scala.game.extensions.cosmetics.CosmeticTickState

class WingParticleEffectState(
    var lastMovement: Long = System.currentTimeMillis()
) : CosmeticTickState
