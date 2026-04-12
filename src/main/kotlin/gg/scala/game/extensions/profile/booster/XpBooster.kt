package gg.scala.game.extensions.profile.booster

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
