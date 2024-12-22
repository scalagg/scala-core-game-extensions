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
object EvilMessageBundle : MessageBundle
{
    override val phrases = listOf(
        "was backstabbed by", "was pushed into the abyss by", "was thrown into a ravine by", "was brutally shot by"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "evil")
    override val name = "Evil"
}
