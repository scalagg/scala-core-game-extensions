package gg.tropic.game.extensions.guilds

import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.acf.annotation.Description
import gg.scala.commons.acf.annotation.HelpCommand
import gg.scala.commons.acf.annotation.Subcommand
import gg.scala.commons.annotations.commands.AssignPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.filter.ChatMessageFilterHandler
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import net.evilblock.cubed.util.CC
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 7/13/2023
 */
@AutoRegister
@CommandPermission("guilds.command.guild")
@CommandAlias("guild|g|club|guilds|clan|clans")
object GuildCommands : ScalaCommand()
{
    @Default
    @HelpCommand
    fun onHelp(help: CommandHelp)
    {
        help.showHelp()
    }

    @Subcommand("leave|disband")
    @Description("Leave or disband your guild.")
    fun onLeave(player: ScalaPlayer) = player.guildIdentity
        .thenCompose {
            if (it.creator.uniqueId == player.uniqueId)
            {
                return@thenCompose GuildService
                    .destroy(it)
                    .thenRun {
                        player.sendMessage("$prefix{CC.RED}You've deleted your guild!")
                    }
            }

            it
                .removeMember(player.uniqueId)
                .thenRun {
                    player.sendMessage("$prefix${CC.RED}You left the ${CC.BOLD}${it.name}${CC.RED} guild!")
                }
        }

    @AssignPermission
    @Subcommand("create")
    @Description("Create a new guild.")
    fun onCreate(player: ScalaPlayer, name: String) = GuildService
        .guildByUser(player.uniqueId)
        .thenCompose {
            if (it != null)
            {
                throw ConditionFailedException(
                    "You are already in a guild!"
                )
            }

            GuildService.guildByName(name)
        }
        .thenCompose {
            if (it != null)
            {
                throw ConditionFailedException(
                    "A guild by the name ${CC.YELLOW}$name${CC.RED} already exists!"
                )
            }

            if (name.length > 16)
            {
                throw ConditionFailedException(
                    "The guild name must be less than 16 characters!"
                )
            }

            if (
                ChatMessageFilterHandler.handleMessageFilter(
                    player.bukkit(), name, false
                )
            )
            {
                throw ConditionFailedException(
                    "You cannot have profanity in your guild name!"
                )
            }

            // hacky way because save() returns CompletableFuture<Void>
            val guild = Guild(
                name = name,
                creator = GuildMember(
                    player.uniqueId,
                    role = GuildRole.Leader
                )
            )
            guild.save().thenApply { guild }
        }
        .thenAccept {
            player.sendMessage(
                "$prefix${CC.GREEN}You created a new guild with the name: ${CC.GOLD}${it.name}"
            )
        }

    @Subcommand("info")
    @Description("Show information about your guild.")
    fun onInfo(player: ScalaPlayer) = player.guildIdentity
        .thenAccept {
            player.describe(it)
        }

    @Subcommand("find")
    @CommandCompletion("@players")
    @Description("Find a player's guild.")
    fun onFind(player: ScalaPlayer, target: AsyncLemonPlayer) =
        target.validatePlayers(player.bukkit(), false) {
            val guild = GuildService
                .guildByUser(it.uniqueId)
                .join()
                ?: throw ConditionFailedException(
                    "${CC.YELLOW}${it.name}${CC.RED} is not in a guild."
                )

            player.describe(guild)
        }

    private val ScalaPlayer.guildIdentity: CompletableFuture<Guild>
        get() = GuildService
            .guildByUser(uniqueId)
            .thenApply {
                if (it == null)
                {
                    throw ConditionFailedException("You are not in a guild!")
                }

                it
            }

    private fun ScalaPlayer.describe(guild: Guild)
    {
        sendMessage(
            "${CC.GREEN}${guild.name}",
            "$guild"
        )
    }
}
