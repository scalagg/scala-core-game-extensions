package gg.tropic.game.extensions.profile.booster

/**
 * @author GrowlyX
 * @since 7/13/2023
 */
data class XpBooster(
    val multiplier: Float,
    val duration: Long,
    var expiration: Long = -1L
)
{
    fun activate()
    {
        expiration = System.currentTimeMillis() + duration
    }
}
