package gg.scala.game.extensions.cosmetics.messagebundles.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.messagebundles.KillMessageBundleCosmeticCategory

/**
 * @author OhEmilyy
 * @since 8/12/2024
 */
@Service
object MoonMessageBundle : gg.scala.game.extensions.cosmetics.messagebundles.cosmetics.MessageBundle
{
    override val phrases = listOf(
        "was crushed into moon dust by", "was sent the wrong way by", "was hit by an asteroid from", "was blasted to the moon by"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "moon")
    override val name = "Moon"
}
