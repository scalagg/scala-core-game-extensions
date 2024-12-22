package gg.scala.game.extensions.cosmetics

import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
@AutoRegister
@CommandAlias("cosmetic|cosmetics|armor|customizer|chattags|chattag|prefixes|killeffects|killmessages|nametags|tags")
object CosmeticCommand : ScalaCommand()
{
    var preventCosmeticUsage = { player: Player -> false }

    @Default
    fun onDefault(player: ScalaPlayer)
    {
        if (preventCosmeticUsage(player.bukkit()))
        {
            throw ConditionFailedException(
                "You cannot use this command right now!"
            )
        }

        if (!CosmeticLocalConfig.enableCosmeticResources)
        {
            throw ConditionFailedException(
                "This command is unavailable on game servers!"
            )
        }

        CosmeticCategoryMenu().openMenu(player.bukkit())
    }
}
