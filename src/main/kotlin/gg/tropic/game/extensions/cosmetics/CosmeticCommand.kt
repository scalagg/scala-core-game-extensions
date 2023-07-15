package gg.tropic.game.extensions.cosmetics

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer

/**
 * @author GrowlyX
 * @since 7/14/2023
 */
@AutoRegister
@CommandAlias("cosmetic|cosmetics")
object CosmeticCommand : ScalaCommand()
{
    @Default
    fun onDefault(player: ScalaPlayer)
    {
        CosmeticCategoryMenu().openMenu(player.bukkit())
    }
}
