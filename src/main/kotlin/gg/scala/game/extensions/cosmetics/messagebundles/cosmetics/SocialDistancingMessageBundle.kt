package gg.scala.game.extensions.cosmetics.messagebundles.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.messagebundles.KillMessageBundleCosmeticCategory

@Service
object SocialDistancingMessageBundle : MessageBundle
{
    override val phrases = listOf(
        "was too shy to meet", "didn't distant themselves properly from", "was coughed at by", "tripped while trying to run away from"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "socialdistancing")
    override val name = "SocialDistancing"
}
