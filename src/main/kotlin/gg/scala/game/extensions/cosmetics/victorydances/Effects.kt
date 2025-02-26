package gg.scala.game.extensions.cosmetics.victorydances

import de.slikey.effectlib.EffectManager
import me.lucko.helper.Helper

/**
 * @author Topu
 * @date 2/26/2025
 */
object Effects
{
    private val manager = EffectManager(Helper.hostPlugin())
    fun get() = manager
}
