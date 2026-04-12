package gg.scala.game.extensions.leveling

import java.util.UUID

/**
 * @author Limo
 * @date 2/11/2026
 */
data class XpRange(
    val id: String = UUID.randomUUID().toString().substring(0, 8),
    var minLevel: Int = 0,
    var maxLevel: Int = 10,
    var xpRequired: Long = 1000L,
    var description: String = ""
)
