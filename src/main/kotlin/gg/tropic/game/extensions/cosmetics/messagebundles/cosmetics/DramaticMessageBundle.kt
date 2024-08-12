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
object DramaticMessageBundle : MessageBundle
{
    override val phrases = listOf(
        "was tragically backstabbed by", "was heartlessly let go by", "had their heart pierced by", "was delivered into nothingness by", "was dismembered by"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "dramatic")
    override val name = "Dramatic"
}
