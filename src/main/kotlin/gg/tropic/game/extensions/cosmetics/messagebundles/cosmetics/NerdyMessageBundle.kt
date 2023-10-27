package gg.tropic.game.extensions.cosmetics.messagebundles.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.tropic.game.extensions.cosmetics.CosmeticRegistry
import gg.tropic.game.extensions.cosmetics.ImmutableResource
import gg.tropic.game.extensions.cosmetics.messagebundles.KillMessageBundleCosmeticCategory

/**
 * @author GrowlyX
 * @since 10/26/2023
 */
@Service
object NerdyMessageBundle : MessageBundle
{
    override val phrases = listOf(
        "ALT+F4'd",
        "deleted",
        "crashed",
        "ratted",
        "hacked",
        "over-heated"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "nerdy")
    override val name = "Nerdy"
}
