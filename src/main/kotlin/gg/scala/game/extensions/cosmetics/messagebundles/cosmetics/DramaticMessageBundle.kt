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
object DramaticMessageBundle : gg.scala.game.extensions.cosmetics.messagebundles.cosmetics.MessageBundle
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
