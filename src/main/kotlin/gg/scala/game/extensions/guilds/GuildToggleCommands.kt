package gg.scala.game.extensions.guilds

import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.visibility.VisibilityHandler

@AutoRegister
object GuildToggleCommands : ScalaCommand()
{
    @CommandAlias(
        "toggleguildinvites|guildinvites|tgi"
    )
    @CommandPermission("guilds.command.guild")
    fun onTogglePlayerVisibility(player: ScalaPlayer)
    {
        val profile = BasicsProfileService.find(player.bukkit())
            ?: throw ConditionFailedException(
                "Sorry, your profile did not load properly."
            )

        val messagesRef = profile.settings["guilds:invites"]!!
        val mapped = messagesRef.map<StateSettingValue>()

        if (mapped == StateSettingValue.ENABLED)
        {
            messagesRef.value = "DISABLED"

            player.sendMessage(
                "${CC.RED}You will no longer receive guild invites."
            )
        } else
        {
            messagesRef.value = "ENABLED"

            player.sendMessage(
                "${CC.GREEN}You can now receive guild invites."
            )
        }

        profile.save()
    }
}
