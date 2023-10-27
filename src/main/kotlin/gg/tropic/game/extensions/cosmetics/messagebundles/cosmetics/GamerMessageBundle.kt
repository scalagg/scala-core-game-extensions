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
object GamerMessageBundle : MessageBundle
{
    override val phrases = listOf(
        "destroyed", "rolled",
        "shoved into a locker", "clapped"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "gamer")
    override val name = "Gamer"
}
