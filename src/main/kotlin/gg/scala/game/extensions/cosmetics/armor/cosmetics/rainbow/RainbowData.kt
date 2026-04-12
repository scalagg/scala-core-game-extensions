package gg.scala.game.extensions.cosmetics.armor.cosmetics.rainbow

import gg.scala.game.extensions.cosmetics.CosmeticTickState
import org.bukkit.Color

data class RainbowData(
    var colorHsl: Double = 0.0,
    var color: Color = hue2rgb(0.0),
    var nextBroadcast: Long = System.currentTimeMillis() + 200L
) : CosmeticTickState

fun hue2rgb(h: Double): Color
{
    var r = 0.0
    var g = 0.0
    var b = 0.0
    val i = (h * 6).toInt()
    val f = h * 6 - i
    val q = 1 - f
    when (i % 6) {
        0 -> {
            r = 1.0
            g = f
        }
        1 -> {
            r = q
            g = 1.0
        }
        2 -> {
            g = 1.0
            b = f
        }
        3 -> {
            g = q
            b = 1.0
        }
        4 -> {
            r = f
            b = 1.0
        }
        5 -> {
            r = 1.0
            b = q
        }
    }
    return Color.fromRGB((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt())
}
