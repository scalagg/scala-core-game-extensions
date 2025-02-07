package gg.scala.game.extensions.namemc

import java.util.UUID

/**
 * @author Topu
 * @date 2/7/2025
 */
data class NameMCVoteRequest(
    val uniqueId: UUID,
    val future: (Boolean) -> Unit
)
