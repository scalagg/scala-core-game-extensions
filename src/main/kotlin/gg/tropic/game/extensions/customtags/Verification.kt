package gg.tropic.game.extensions.customtags

import java.util.UUID

data class Verification(
    val status: VerificationStatus,
    val verifier: UUID,
    val timestamp: Long
)
{

}
