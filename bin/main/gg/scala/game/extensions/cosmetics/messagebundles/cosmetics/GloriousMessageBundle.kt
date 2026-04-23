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
object GloriousMessageBundle : MessageBundle
{
    override val phrases = listOf(
        "was stomped by", "was thrown down a pit by", "was thrown to the ground by", "was out classed by"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "glorious")
    override val name = "Glorious"
}
