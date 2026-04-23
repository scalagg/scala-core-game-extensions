package gg.scala.game.extensions.leveling

import java.util.UUID

/**
 * Defines an XP range for the RANGE_BASED strategy.
 * Each range specifies how much XP is needed for levels within the range.
 *
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
