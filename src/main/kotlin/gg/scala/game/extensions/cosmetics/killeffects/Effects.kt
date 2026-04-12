package gg.scala.game.extensions.cosmetics.killeffects

import de.slikey.effectlib.EffectManager
import me.lucko.helper.Helper

object Effects
{
    private val manager = EffectManager(Helper.hostPlugin())
    fun get() = manager
}
