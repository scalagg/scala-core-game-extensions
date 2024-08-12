package gg.tropic.game.extensions.cosmetics.messagebundles.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.messagebundles.KillMessageBundleCosmeticCategory

/**
 * @author OhEmilyy
 * @since 8/12/2024
 */
@Service
object MemesMessageBundle : MessageBundle
{
    override val phrases = listOf(
        "shown the wae", "oofed", "memed", "shown the door"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "memes")
    override val name = "Memes"
}
