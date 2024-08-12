package gg.tropic.game.extensions.cosmetics.killeffects

import de.slikey.effectlib.EffectManager
import me.lucko.helper.Helper

/**
 * @author GrowlyX
 * @since 8/12/2024
 */
object Effects
{
    private val manager = EffectManager(Helper.hostPlugin())
    fun get() = manager
}
