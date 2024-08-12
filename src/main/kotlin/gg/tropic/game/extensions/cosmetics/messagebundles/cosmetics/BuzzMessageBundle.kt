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
object BuzzMessageBundle : MessageBundle
{
    override val phrases = listOf(
        "was buzzed to death by", "was bzzz'd into oblevion by", "was startled by", "was stung to death by"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "buzz")
    override val name = "Buzz"
}
