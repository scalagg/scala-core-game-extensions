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
object PrimalMessageBundle : MessageBundle
{
    override val phrases = listOf(
        "was hunted down by", "stumbled on a trap set by", "got skewered by", "was thrown into a volcano by"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "Primal")
    override val name = "Primal"
}
