package gg.tropic.game.extensions.guilds

import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.*
import gg.scala.commons.annotations.commands.AssignPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.annotations.commands.HighPriority
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.command.annotations.AllowOffline
import gg.scala.lemon.filter.ChatMessageFilterHandler
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import gg.scala.lemon.util.QuickAccess
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.FancyMessage
import net.evilblock.cubed.util.time.TimeUtil
import net.md_5.bungee.api.chat.ClickEvent
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 7/13/2023
 */
@HighPriority
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

    @Subcommand("settings description")
    @Description("Set your guild's description.")
    fun onDescription(player: ScalaPlayer, description: String) = player.guildIdentity
        .thenCompose {
            if (it.member(player.uniqueId).role != GuildRole.Leader)
            {
                throw ConditionFailedException(
                    "You do not have permission to change your guild's description!"
                )
            }

            if (
                ChatMessageFilterHandler.handleMessageFilter(
                    player.bukkit(), description, false
                )
            )
            {
                throw ConditionFailedException(
                    "You cannot have profanity in your guild description!"
                )
            }

            it.updateDescription(description)
                .thenRun {
                    player.sendMessage(
                        "$prefix${CC.GREEN}You have updated your guild description to:",
                        description
                    )
                }
        }

    @Subcommand("settings allinvite")
    @Description("Enable/disable all invite for your guild.")
    fun onAllInvite(player: ScalaPlayer) = player.guildIdentity
        .thenCompose {
            if (it.member(player.uniqueId).role != GuildRole.Leader)
            {
                throw ConditionFailedException(
                    "You do not have permission to change your guild's allinvite setting!"
                )
            }

            it.updateAllInvite()
                .thenRun {
                    player.sendMessage(
                        "$prefix${
                            if (it.allInvite) "${CC.GREEN}You have enabled all invite!" else "${CC.RED}You have disabled all invite!"
                        }"
                    )
                }
        }

    @Subcommand("kick")
    @CommandCompletion("@players")
    @Description("Kick a player from your guild.")
    fun onKick(player: ScalaPlayer, @AllowOffline target: AsyncLemonPlayer) = target
        .validatePlayers(player.bukkit(), false) {
            player.guildIdentity
                .thenCompose { guild ->
                    val role = guild
                        .member(player.uniqueId)
                        .role

                    if (role == GuildRole.Member)
                    {
                        throw ConditionFailedException(
                            "You do not have permission to kick players from your guild!"
                        )
                    }

                    if (it.uniqueId == player.uniqueId)
                    {
                        throw ConditionFailedException(
                            "You cannot kick yourself from a guild!"
                        )
                    }

                    if (guild.creator.uniqueId == it.uniqueId)
                    {
                        throw ConditionFailedException(
                            "You cannot kick the leader of the guild!"
                        )
                    }

                    if (!guild.members.containsKey(it.uniqueId))
                    {
                        throw ConditionFailedException(
                            "${CC.YELLOW}${it.name}${CC.RED} is not in your guild!"
                        )
                    }

                    guild
                        .removeMember(it.uniqueId)
                        .thenCompose { _ ->
                            guild.sendMessage(
                                "${CC.YELLOW}${it.name}${CC.RED} was kicked from the guild by ${CC.YELLOW}${player.bukkit().name}${CC.RED}!"
                            )
                        }
                }
                .join()
        }

    @Subcommand("transfer")
    @CommandCompletion("@players")
    @Description("Transfer your guild to another player.")
    fun onTransfer(player: ScalaPlayer, @AllowOffline target: AsyncLemonPlayer) = target
        .validatePlayers(player.bukkit(), false) {
            player.guildIdentity
                .thenCompose { guild ->
                    val member = guild
                        .member(player.uniqueId)

                    if (member.role == GuildRole.Leader)
                    {
                        throw ConditionFailedException(
                            "You do not have permission to transfer the guild!"
                        )
                    }

                    if (it.uniqueId == player.uniqueId)
                    {
                        throw ConditionFailedException(
                            "You cannot transfer the guild to yourself!"
                        )
                    }

                    if (!guild.members.containsKey(it.uniqueId))
                    {
                        throw ConditionFailedException(
                            "${CC.YELLOW}${it.name}${CC.RED} is not in your guild!"
                        )
                    }

                    val targetMember = guild.member(it.uniqueId)
                    guild.members.remove(it.uniqueId)

                    targetMember.role = GuildRole.Leader
                    member.role = GuildRole.Admin

                    guild.creator = targetMember
                    guild.members[player.bukkit().uniqueId] = member
                    guild.save().thenApply { guild }
                }
                .thenCompose { guild ->
                    guild.sendMessage(
                        "${CC.RED}The guild was transferred to: ${CC.BOLD}${it.name}${CC.RED}!"
                    )
                }
                .join()
        }

    @CommandAlias("guildchat|gc|gchat")
    @Subcommand("chat")
    @Description("Send a chat message to your guild members")
    fun onGuildChat(player: ScalaPlayer, message: String) = player.guildIdentity
        .thenCompose { guild ->
            if (guild == null) {
                throw ConditionFailedException(
                    "You are not in a guild!"
                )
            }

            guild.sendMessage(
                "${CC.GREEN}${player.bukkit().name}: ${CC.WHITE}$message"
            )
        }

    @Subcommand("settings promote|demote|role")
    @CommandCompletion("@players *")
    @Description("Promote/demote a player in your guild.")
    fun onRole(player: ScalaPlayer, @AllowOffline target: AsyncLemonPlayer, newRole: GuildRole) = target
        .validatePlayers(player.bukkit(), false) {
            if (player.uniqueId == it.uniqueId)
            {
                throw ConditionFailedException(
                    "You cannot manage your self role!"
                )
            }

            player.guildIdentity
                .thenCompose { guild ->
                    val role = guild
                        .member(player.uniqueId)
                        .role

                    if (role != GuildRole.Leader)
                    {
                        throw ConditionFailedException(
                            "You do not have permission to manage player roles in your guild!"
                        )
                    }

                    if (newRole == GuildRole.Leader)
                    {
                        throw ConditionFailedException(
                            "You cannot set another user as the guild leader! Use ${CC.BOLD}/guild transfer${CC.RED} to transfer your guild."
                        )
                    }

                    if (!guild.members.containsKey(it.uniqueId))
                    {
                        throw ConditionFailedException(
                            "${CC.YELLOW}${it.name}${CC.RED} is not a member of your guild!"
                        )
                    }

                    guild.member(it.uniqueId).role = newRole
                    guild.save().thenApply { guild }
                }
                .thenCompose { guild ->
                    guild.sendMessage(
                        "${CC.YELLOW}${it.name}'s${CC.GREEN} guild role has been set to: ${CC.AQUA}${newRole.name}"
                    )
                }
                .join()

        }

    @Subcommand("invite")
    @CommandCompletion("@players")
    @Description("Invite a player to your guild.")
    fun onInvite(player: ScalaPlayer, @AllowOffline target: AsyncLemonPlayer) = target
        .validatePlayers(player.bukkit(), false) {
            player.guildIdentity
                .thenCompose { guild ->
                    val role = guild
                        .member(player.uniqueId)
                        .role

                    if (!guild.allInvite && role == GuildRole.Member)
                    {
                        throw ConditionFailedException(
                            "You do not have permission to invite players to your guild!"
                        )
                    }

                    if (it.uniqueId == player.uniqueId)
                    {
                        throw ConditionFailedException(
                            "You cannot invite yourself to a guild!"
                        )
                    }

                    if (guild.members.containsKey(it.uniqueId) || guild.creator.uniqueId == it.uniqueId)
                    {
                        throw ConditionFailedException(
                            "${CC.YELLOW}${it.name}${CC.RED} is already in your guild!"
                        )
                    }

                    if (guild.outgoingInvitations.containsKey(it.uniqueId))
                    {
                        val invitation = guild.outgoingInvitations[it.uniqueId]!!
                        if (invitation > System.currentTimeMillis())
                        {
                            throw ConditionFailedException(
                                "There is already an invite to ${CC.YELLOW}${it.name}${CC.RED}. Please wait ${
                                    TimeUtil.formatIntoAbbreviatedString(((invitation - System.currentTimeMillis()) / 1000).toInt())
                                } before inviting the user again."
                            )
                        }
                    }

                    GuildService
                        .guildRequestsDisabled(it.uniqueId)
                        .thenCompose { profile ->
                            if (profile != null)
                            {
                                throw ConditionFailedException(
                                    "${CC.YELLOW}${it.name}${CC.RED} has their guild invites disabled!"
                                )
                            }

                            guild.addOutgoingInvite(it.uniqueId)
                        }
                        .thenCompose {  _ ->
                            guild.sendMessage(
                                "${CC.YELLOW}${it.name}${CC.GREEN} was invited to the guild!"
                            )
                        }
                        .thenApply { guild }
                }
                .thenCompose { guild ->
                    QuickAccess
                        .sendGlobalPlayerFancyMessage(
                            FancyMessage()
                                .withMessage(
                                    "$prefix${CC.WHITE}${player.bukkit().displayName}${CC.GREEN} sent you an invite to join the guild ${CC.YELLOW}${guild.name}${CC.GREEN}!"
                                )
                                .withMessage(" ${CC.I_GRAY}(Click to join)")
                                .andHoverOf(
                                    "${CC.GREEN}Join the ${guild.name} guild!"
                                )
                                .andCommandOf(
                                    ClickEvent.Action.RUN_COMMAND,
                                    "/guild join ${guild.name}"
                                ),
                            it.uniqueId
                        )
                }
                .join()
        }

    @Subcommand("leave|disband")
    @Description("Leave or disband your guild.")
    fun onLeave(player: ScalaPlayer) = player.guildIdentity
        .thenCompose {
            if (it.creator.uniqueId == player.uniqueId)
            {
                return@thenCompose GuildService
                    .destroy(it)
                    .thenCompose {  _ ->
                        it.sendMessage(
                            "${CC.RED}Your guild was deleted! You are no longer in any guild."
                        )
                    }
                    .thenRun {
                        player.sendMessage("$prefix${CC.RED}You've deleted your guild!")
                    }
            }

            it
                .removeMember(player.uniqueId)
                .thenCompose { _ ->
                    it.sendMessage(
                        "${CC.YELLOW}${player.bukkit().name}${CC.RED} left the guild!"
                    )
                }
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
    fun onFind(player: ScalaPlayer, @AllowOffline target: AsyncLemonPlayer) =
        target.validatePlayers(player.bukkit(), false) {
            val guild = GuildService
                .guildByUser(it.uniqueId)
                .join()
                ?: throw ConditionFailedException(
                    "${CC.YELLOW}${it.name}${CC.RED} is not in a guild."
                )

            player.describe(guild)
        }

    @Subcommand("show")
    @Description("Show info about a particular guild.")
    fun onFind(player: ScalaPlayer, name: String) =
        GuildService
            .guildByName(name)
            .thenAccept {
                if (it == null)
                {
                    throw ConditionFailedException(
                        "No guild by the name ${CC.YELLOW}$name${CC.RED} exists."
                    )
                }

                player.describe(it)
            }

    @Subcommand("join")
    @Description("Join a guild you have an invite to.")
    fun onJoin(player: ScalaPlayer, name: String) =
        GuildService
            .guildByName(name)
            .thenAccept { guild ->
                if (
                    GuildService
                        .guildByUser(player.uniqueId)
                        .join() != null
                )
                {
                    throw ConditionFailedException(
                        "You are already in a guild. You must leave your guild before joining a new one."
                    )
                }

                if (guild == null)
                {
                    throw ConditionFailedException(
                        "No guild by the name ${CC.YELLOW}$name${CC.RED} exists."
                    )
                }

                if (guild.outgoingInvitations.containsKey(player.uniqueId))
                {
                    val invitation = guild.outgoingInvitations[player.uniqueId]!!
                    if (invitation < System.currentTimeMillis())
                    {
                        throw ConditionFailedException(
                            "Your invite to the guild ${CC.YELLOW}${guild.name}${CC.RED} has expired."
                        )
                    }
                } else
                {
                    throw ConditionFailedException(
                        "You do not have an invite to the guild ${CC.YELLOW}${guild.name}${CC.RED}!"
                    )
                }

                guild.outgoingInvitations.remove(player.uniqueId)
                guild
                    .addMember(player.uniqueId)
                    .thenCompose { _ ->
                        guild.sendMessage(
                            "${CC.YELLOW}${player.bukkit().name}${CC.GREEN} joined the guild!"
                        )
                    }
                    .thenAccept {
                        player.sendMessage("$prefix${CC.GREEN}You joined the guild ${CC.YELLOW}${guild.name}${CC.GREEN}!")
                    }
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
        sendMessage("${CC.GREEN}${guild.name}")

        guild.description?.apply {
            sendMessage(this)
            sendMessage("")
        }

        sendMessage(CC.GRAY + "ID: " + CC.WHITE + guild.id)
        sendMessage(CC.GRAY + "Created: " + CC.WHITE + TimeUtil.formatIntoDateString(guild.createdOn))
        sendMessage(CC.GRAY + "Members: " + CC.GREEN + (guild.members.size + 1) + "/∞")
        sendMessage(CC.GRAY + "Leader: " + CC.PRI + QuickAccess.fetchColoredName(guild.creator.uniqueId) + " " + (if (guild.creator.online().join()) CC.GREEN else CC.RED) + "■")
        sendMessage("  ")

        for (value in GuildRole.values())
        {
            if (value !== GuildRole.Leader)
            {
                val roleMembers = guild.members.values
                    .filter {
                        it.role == value
                    }

                val prefix = CC.WHITE + value.name + "s (${roleMembers.size}): "

                val finalMessage = prefix + if (roleMembers.isEmpty())
                    "${CC.RED}None" else roleMembers
                    .joinToString(separator = "${CC.GRAY}, ") {
                        QuickAccess.fetchColoredName(it.uniqueId) + " " + (if (it.online().join()) CC.GREEN else CC.RED) + "■"
                    }

                sendMessage(finalMessage)
            }
        }
    }
}
