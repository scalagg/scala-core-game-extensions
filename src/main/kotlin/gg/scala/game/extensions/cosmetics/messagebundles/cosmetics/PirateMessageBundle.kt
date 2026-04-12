package gg.scala.game.extensions.cosmetics.messagebundles.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.cosmetics.CosmeticRegistry
import gg.scala.game.extensions.cosmetics.ImmutableResource
import gg.scala.game.extensions.cosmetics.messagebundles.KillMessageBundleCosmeticCategory

@Service
object PirateMessageBundle : MessageBundle
{
    override val phrases = listOf(
        "be sent to Davy Jones'' locker by", "be cannonballed to death by", "be shot and killed by", "be shot and killed by", "be killed with magic by", "be killed with metal by"
    )

    @Configure
    fun configure()
    {
        CosmeticRegistry.register(
            KillMessageBundleCosmeticCategory,
            this
        )
    }

    override val resource = ImmutableResource("messagebundles", "pirate")
    override val name = "Pirate"
}
