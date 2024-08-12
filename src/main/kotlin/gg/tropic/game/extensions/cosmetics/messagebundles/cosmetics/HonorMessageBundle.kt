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
object HonorMessageBundle : MessageBundle
{
    override val phrases = listOf(
        "died in close combat to", "fought to the edge with", "fell to the great warrior", "stumbled off a ledge with the help by"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "honor")
    override val name = "Honor"
}
