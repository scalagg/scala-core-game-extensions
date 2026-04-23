package gg.scala.game.extensions.profile.menu

import com.cryptomorin.xseries.XMaterial
import com.google.gson.JsonObject
import gg.scala.friends.handler.KFriendHandler
import gg.scala.friends.handler.KPlayerHandler
import gg.scala.friends.menu.FriendUIService.plugin
import gg.scala.game.extensions.economy.EconomyProfileService
import gg.scala.game.extensions.guilds.Guild
import gg.scala.game.extensions.guilds.GuildService
import gg.scala.game.extensions.profile.booster.XpBoosterMenu
import gg.scala.game.extensions.profile.levelSymbol
import gg.scala.game.extensions.profile.profile
import gg.scala.lemon.LemonConstants
import gg.scala.lemon.util.CubedCacheUtil
import gg.scala.lemon.util.QuickAccess
import net.evilblock.cubed.ScalaCommonsSpigot
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.Menu
import net.evilblock.cubed.serializers.Serializers
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import net.evilblock.cubed.util.math.Numbers
import net.evilblock.cubed.util.time.TimeUtil
import net.wesjd.anvilgui.AnvilGUI
import org.apache.commons.lang3.time.DurationFormatUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.CompletableFuture
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.roundToLong

enum class ProfileCategory(
    val slot: Int,
    val title: String,
    val description: String
)
{
    PROFILE(2, "Profile", "Account, progress, and settings"),
    FRIENDS(3, "Friends", "Manage friends and requests"),
    PARTY(4, "Party", "View your current party"),
    GUILD(5, "Guild", "Manage guild and members"),
    RECENT(6, "Recent Players", "Players from recent matches")
}

private data class PartySnapshot(
    val partyId: UUID,
    val leaderId: UUID,
    val memberIds: List<UUID>,
    val limit: Int,
    val status: String
)

/**
 * @author Limo
 * @date 2/13/2026
 */
class ProfileMenu(
    private val selectedCategory: ProfileCategory = ProfileCategory.PROFILE
) : Menu("${selectedCategory.title} - Profile")
{
    override fun size(buttons: Map<Int, Button>) = 54

    override fun getButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        populateCategoryHeader(player, buttons)

        when (selectedCategory)
        {
            ProfileCategory.PROFILE -> populateProfileCategory(player, buttons)
            ProfileCategory.FRIENDS -> populateFriendsCategory(player, buttons)
            ProfileCategory.PARTY -> populatePartyCategory(player, buttons)
            ProfileCategory.GUILD -> populateGuildCategory(player, buttons)
            ProfileCategory.RECENT -> populateRecentPlayersCategory(player, buttons)
        }

        return buttons
    }

    private fun populateCategoryHeader(player: Player, buttons: MutableMap<Int, Button>)
    {
        val darkPane = ItemBuilder
            .of(Material.STAINED_GLASS_PANE)
            .data(15)
            .name("${CC.D_GRAY} ")
            .toButton()

        val graySeparator = ItemBuilder
            .of(Material.STAINED_GLASS_PANE)
            .data(7)
            .name("${CC.GRAY} ")
            .toButton()

        val greenIndicator = ItemBuilder
            .of(Material.STAINED_GLASS_PANE)
            .data(5)
            .name("${CC.GREEN}${selectedCategory.title}")
            .toButton()

        for (slot in listOf(0, 1, 7, 8))
        {
            buttons[slot] = darkPane
        }

        for (category in ProfileCategory.entries)
        {
            buttons[category.slot] = createCategoryButton(player, category)
        }

        for (slot in 9..17)
        {
            buttons[slot] = graySeparator
        }

        buttons[selectedCategory.slot + 9] = greenIndicator
    }

    private fun createCategoryButton(player: Player, category: ProfileCategory): Button
    {
        val item = when (category)
        {
            ProfileCategory.FRIENDS -> textureHead(pinkWoolHeadTexture)
            ProfileCategory.PARTY -> textureHead(lightBlueWoolHeadTexture)
            ProfileCategory.GUILD -> textureHead(greenWoolHeadTexture)
            ProfileCategory.RECENT -> textureHead(grayWoolHeadTexture)
            ProfileCategory.PROFILE -> playerHead(player.name)
        }

        val lore = when (category)
        {
            ProfileCategory.FRIENDS -> listOf(
                "${CC.GRAY}View your Glacial friends' profiles,",
                "${CC.GRAY}and interact with your online friends!"
            )
            ProfileCategory.PARTY -> listOf(
                "${CC.GRAY}Create a party and join up with",
                "${CC.GRAY}other players to play games together!"
            )
            ProfileCategory.GUILD -> listOf(
                "${CC.GRAY}Form a guild with other players",
                "${CC.GRAY}and work towards shared rewards."
            )
            ProfileCategory.RECENT -> listOf(
                "${CC.GRAY}View players you have played recent",
                "${CC.GRAY}games with."
            )
            ProfileCategory.PROFILE -> listOf(
                "${CC.GRAY}Access your account profile,",
                "${CC.GRAY}leveling, stats, and settings."
            )
        }

        val selectionLine = if (selectedCategory == category)
            "${CC.GREEN}Currently selected"
        else
            "${CC.YELLOW}Click to switch"

        return item
            .name(
                if (selectedCategory == category) "${CC.GREEN}${category.title}"
                else "${CC.WHITE}${category.title}"
            )
            .addToLore(*(lore + "" + selectionLine).toTypedArray())
            .toButton { _, _ ->
                if (selectedCategory == category)
                {
                    Button.playNeutral(player)
                    return@toButton
                }

                Button.playNeutral(player)

                if (category == ProfileCategory.PROFILE)
                {
                    ProfileMenu().openMenu(player)
                    return@toButton
                }

                ProfileMenu(category).openMenu(player)
            }
    }

    private fun populateProfileCategory(player: Player, buttons: MutableMap<Int, Button>)
    {
        val profile = player.profile
        val progressPercent = profile.getPercentageCompleteUntilNextLevel().coerceIn(0.0f, 100.0f)
        val levelXpBand = profile.getXpRequiredToAdvanceToLevel(profile.level + 1).toDouble()
        val xpUntilNext = max(
            0L,
            (((100.0 - progressPercent) / 100.0) * levelXpBand).roundToLong()
        )

        val rank = runCatching {
            gg.scala.lemon.handler.PlayerHandler
                .find(player.uniqueId)
                ?.activeGrant
                ?.getRank()
                ?.getColoredName()
        }.getOrNull() ?: "${CC.GRAY}None"

        val questsCompleted = playerMetaLong(player.uniqueId, "quests-completed")
        val karma = playerMetaLong(player.uniqueId, "karma")
        val economyProfile = EconomyProfileService.find(player)

        buttons[18] = ItemBuilder
            .of(XMaterial.BOOK)
            .name("${CC.GREEN}Profile Overview")
            .addToLore(
                "${CC.GRAY}This category contains account and",
                "${CC.GRAY}progression tools."
            )
            .toButton()

        buttons[20] = playerHead(player.name)
            .name("${CC.GREEN}Character Information")
            .addToLore(
                "${CC.GRAY}Rank: $rank",
                "${CC.GRAY}Level: ${CC.GOLD}${profile.level}$levelSymbol",
                "${CC.GRAY}Experience until next Level: ${CC.YELLOW}${Numbers.format(xpUntilNext)}",
                "${CC.GRAY}Quests Completed: ${CC.GOLD}${Numbers.format(questsCompleted)}",
                "${CC.GRAY}Karma: ${CC.D_AQUA}${Numbers.format(karma)}",
                "${CC.GRAY}Glacial Gold: ${CC.GOLD}${Numbers.format(economyProfile?.balance("shards") ?: 0)}",
                "",
                "${CC.YELLOW}Click to see the Glacial Store link."
            )
            .toButton { _, _ ->
                Button.playNeutral(player)
                player.sendMessage("${CC.GREEN}Store: ${CC.GOLD}${LemonConstants.STORE_LINK}")
            }

        buttons[21] = ItemBuilder
            .of(Material.REDSTONE_COMPARATOR)
            .name("${CC.GREEN}Settings & Visibility")
            .addToLore(
                "${CC.GRAY}Allows you to edit and control",
                "${CC.GRAY}various personal settings.",
                "",
                "${CC.YELLOW}Click to edit your settings!"
            )
            .toButton { _, _ ->
                executeCommand(player, "settings", "Settings are currently unavailable.")
            }

        buttons[22] = ItemBuilder
            .of(XMaterial.LEATHER_CHESTPLATE)
            .name("${CC.GREEN}Customize Appearances")
            .addToLore(
                "${CC.GRAY}Customize the following visual options",
                "${CC.GRAY}for your player!",
                "${CC.WHITE}• MVP+ Rank Color",
                "${CC.WHITE}• Punch Messages",
                "${CC.WHITE}• Glow",
                "${CC.WHITE}• Status",
                "",
                "${CC.YELLOW}Click to view!"
            )
            .toButton { _, _ ->
                executeCommand(player, "customizer", "Customizer is currently unavailable.")
            }

        buttons[23] = ItemBuilder
            .of(XMaterial.PAPER)
            .name("${CC.GREEN}Stats Viewer")
            .addToLore(
                "${CC.GRAY}Showcases your stats for each",
                "${CC.GRAY}game and an overview of all.",
                "",
                "${CC.YELLOW}Click to view your stats!"
            )
            .toButton { _, _ ->
                executeCommand(player, "stats", "Stats viewer is currently unavailable.")
            }

        buttons[24] = ItemBuilder
            .of(XMaterial.DIAMOND)
            .name("${CC.GREEN}Achievements")
            .addToLore(
                "${CC.GRAY}Track your progress as you unlock",
                "${CC.GRAY}Achievements and rack up points.",
                "",
                "${CC.YELLOW}Click to view your achievements!"
            )
            .toButton { _, _ ->
                executeCommand(player, "achievements", "Achievements are currently unavailable.")
            }

        buttons[29] = ItemBuilder
            .of(XMaterial.BREWING_STAND)
            .name("${CC.GREEN}Glacial Leveling")
            .addToLore(
                "${CC.GRAY}Playing games and completing quests",
                "${CC.GRAY}will reward you with ${CC.AQUA}Glacial",
                "${CC.AQUA}Experience${CC.GRAY}, which is required to",
                "${CC.GRAY}level up and acquire new perks and",
                "${CC.GRAY}rewards!",
                "",
                "${CC.AQUA}Glacial Level ${CC.YELLOW}${profile.level}$levelSymbol ${buildProgressBar(progressPercent)} ${CC.AQUA}${progressPercent.roundToInt()}%",
                "",
                "${CC.GRAY}Experience until next level: ${CC.AQUA}${Numbers.format(xpUntilNext)}",
                "",
                "${CC.YELLOW}Click to see your rewards!"
            )
            .toButton { _, _ ->
                executeCommand(player, "level", "Leveling information is currently unavailable.")
            }

        buttons[30] = ItemBuilder
            .of(XMaterial.POTION)
            .name("${CC.GREEN}Coin Boosters")
            .addToLore(
                "${CC.GRAY}Activate your personal and",
                "${CC.GRAY}network boosters for extra coins.",
                "",
                "${CC.YELLOW}Click to activate boosters!"
            )
            .toButton { _, _ ->
                Button.playNeutral(player)
                XpBoosterMenu().openMenu(player)
            }

        buttons[31] = ItemBuilder
            .of(XMaterial.GOLD_INGOT)
            .name("${CC.GREEN}Glacial Store")
            .addToLore(
                "${CC.GRAY}View the Glacial Store from right",
                "${CC.GRAY}here in-game!",
                "${CC.GRAY}Your Glacial Shards: ${CC.GOLD}${Numbers.format(economyProfile?.balance("shards") ?: 0)}",
                "",
                "${CC.YELLOW}Click to view!"
            )
            .toButton { _, _ ->
                Button.playNeutral(player)
                player.sendMessage("${CC.GREEN}Store: ${CC.GOLD}${LemonConstants.STORE_LINK}")
            }

        buttons[32] = ItemBuilder
            .of(XMaterial.DARK_OAK_DOOR)
            .name("${CC.GREEN}Go to Housing")
            .addToLore(
                "${CC.GRAY}Visit your housing and customize",
                "${CC.GRAY}your own world.",
                "",
                "${CC.YELLOW}Click to view!"
            )
            .toButton { _, _ ->
                executeCommand(player, "housing", "Housing is currently unavailable.")
            }

        buttons[38] = ItemBuilder
            .of(XMaterial.ANVIL)
            .name("${CC.GREEN}Account Status")
            .addToLore(
                "${CC.GRAY}Check your punishment history and",
                "${CC.GRAY}see where you stand.",
                "",
                "${CC.YELLOW}Click to view!"
            )
            .toButton { _, _ ->
                executeCommand(player, "punishments", "Account status is currently unavailable.")
            }

        buttons[39] = ItemBuilder
            .of(XMaterial.BOOK)
            .name("${CC.GREEN}Recent Games")
            .addToLore(
                "${CC.GRAY}View your recently played games.",
                "",
                "${CC.YELLOW}Click to view!"
            )
            .toButton { _, _ ->
                executeCommand(player, "recentgames", "Recent games are currently unavailable.")
            }
    }

    private fun populateFriendsCategory(player: Player, buttons: MutableMap<Int, Button>)
    {
        val friendIds = loadFriendIds(player)
        val onlineCount = friendIds.count { isOnline(it) }
        val incomingRequests = loadIncomingFriendRequests(player.uniqueId)
        val outgoingRequests = loadOutgoingFriendRequests(player.uniqueId)

        buttons[18] = textureHead(pinkWoolHeadTexture)
            .name("${CC.GREEN}Friends Category")
            .addToLore(
                "${CC.GRAY}Browse your friends, requests,",
                "${CC.GRAY}and send new friend requests."
            )
            .toButton()

        buttons[20] = ItemBuilder
            .of(XMaterial.BOOK)
            .name("${CC.GREEN}Friends Overview")
            .addToLore(
                "${CC.GRAY}Total friends: ${CC.WHITE}${friendIds.size}",
                "${CC.GRAY}Online now: ${CC.GREEN}$onlineCount",
                "${CC.GRAY}Offline now: ${CC.RED}${friendIds.size - onlineCount}"
            )
            .toButton()

        buttons[21] = ItemBuilder
            .of(XMaterial.NAME_TAG)
            .name("${CC.GREEN}Add Friend")
            .addToLore(
                "${CC.GRAY}Send a friend request from this menu.",
                "",
                "${CC.YELLOW}Click to type a username"
            )
            .toButton { player, _ ->
                AnvilGUI.Builder()
                    .onClose {
                        Tasks.delayed(1L) {
                            ProfileMenu().openMenu(player!!)
                        }
                    }
                    .onComplete { _, u ->
                        val uniqueId = CubedCacheUtil
                            .fetchUuid(u)
                            ?: return@onComplete kotlin.run {
                                player!!.sendMessage("${CC.RED}Invalid player!")
                                AnvilGUI.Response.close()
                            }

                        KFriendHandler
                            .attemptDispatchFriendRequestTo(
                                player!!, uniqueId
                            )
                            .exceptionally {
                                if (it.message!!.startsWith(
                                        "gg.scala.commons.acf.ConditionFailedException: "
                                    )
                                )
                                {
                                    player.sendMessage(
                                        it.message!!.removePrefix(
                                            "gg.scala.commons.acf.ConditionFailedException: "
                                        )
                                    )
                                }

                                return@exceptionally null
                            }

                        return@onComplete AnvilGUI.Response.close()
                    }
                    .text("Enter a username...")
                    .plugin(plugin)
                    .open(player)
            }

        buttons[22] = ItemBuilder
            .of(XMaterial.NETHER_STAR)
            .name("${CC.GREEN}Friend Requests")
            .addToLore(
                "${CC.GRAY}Incoming: ${CC.YELLOW}${incomingRequests.size}",
                "${CC.GRAY}Outgoing: ${CC.YELLOW}${outgoingRequests.size}",
                "",
                "${CC.YELLOW}Click to view request menu"
            )
            .toButton { _, _ ->
                Button.playNeutral(player)
                openFriendRequestsMenu(player)
            }

        buttons[23] = ItemBuilder
            .of(XMaterial.BOOK)
            .name("${CC.GREEN}Open Friends List")
            .addToLore(
                "${CC.GRAY}Use the default friends list view.",
                "",
                "${CC.YELLOW}Click to open"
            )
            .toButton { _, _ ->
                executeCommand(player, "friend list", "Friends list is currently unavailable.")
            }

        buttons[24] = textureHead(cyanWoolHeadTexture)
            .name("${CC.GREEN}Social Media")
            .addToLore(
                "${CC.GRAY}Click to edit your Social Media links.",
                "",
                "${CC.YELLOW}Click to open"
            )
            .toButton { _, _ ->
                executeCommand(player, "socialmedia", "Social media menu is currently unavailable.")
            }

        for ((index, uuid) in friendIds.take(playerListSlots.size).withIndex())
        {
            val slot = playerListSlots[index]
            val name = safeName(uuid)
            val online = isOnline(uuid)

            buttons[slot] = playerHead(name)
                .name("${if (online) CC.GREEN else CC.RED}$name")
                .addToLore(
                    "${CC.GRAY}Status: ${if (online) "${CC.GREEN}Online" else "${CC.RED}Offline"}",
                    "",
                    "${CC.YELLOW}Click to view profile"
                )
                .toButton { _, _ ->
                    executeCommand(
                        player,
                        "profile $name",
                        "Could not open profile for $name."
                    )
                }
        }

        if (friendIds.isEmpty())
        {
            buttons[31] = ItemBuilder
                .of(XMaterial.BARRIER)
                .name("${CC.RED}No Friends Yet")
                .addToLore(
                    "${CC.GRAY}Use the add friend item to",
                    "${CC.GRAY}send your first request."
                )
                .toButton()
        }
    }

    private fun promptAddFriend(player: Player)
    {
        player.closeInventory()

        InputPrompt()
            .withText("${CC.GREEN}Enter the username to add as a friend:")
            .acceptInput { _, rawText ->
                val text = rawText.trim()

                if (text.isBlank())
                {
                    player.sendMessage("${CC.RED}You must enter a valid username.")
                    Tasks.sync {
                        ProfileMenu(ProfileCategory.FRIENDS).openMenu(player)
                    }
                    return@acceptInput
                }

                val targetId = CubedCacheUtil.fetchUuid(text)
                if (targetId == null)
                {
                    player.sendMessage("${CC.RED}No player named ${CC.YELLOW}$text${CC.RED} was found.")
                    Tasks.sync {
                        ProfileMenu(ProfileCategory.FRIENDS).openMenu(player)
                    }
                    return@acceptInput
                }

                KFriendHandler
                    .attemptDispatchFriendRequestTo(player, targetId)
                    .whenComplete { _, throwable ->
                        Tasks.sync {
                            if (throwable != null)
                            {
                                Button.playFail(player)
                                player.sendMessage("${CC.RED}${readableError(throwable)}")
                            } else
                            {
                                Button.playSuccess(player)
                            }

                            ProfileMenu(ProfileCategory.FRIENDS).openMenu(player)
                        }
                    }
            }
            .start(player)
    }

    private fun populatePartyCategory(player: Player, buttons: MutableMap<Int, Button>)
    {
        val party = loadPartySnapshot(player.uniqueId)

        buttons[18] = textureHead(lightBlueWoolHeadTexture)
            .name("${CC.GREEN}Party Category")
            .addToLore(
                "${CC.GRAY}Track your current party,",
                "${CC.GRAY}members, and invites."
            )
            .toButton()

        if (party == null)
        {
            buttons[20] = ItemBuilder
                .of(XMaterial.BARRIER)
                .name("${CC.RED}You Are Not In A Party")
                .addToLore(
                    "${CC.GRAY}Create a party or accept an invite",
                    "${CC.GRAY}to see party members here."
                )
                .toButton()

            buttons[21] = ItemBuilder
                .of(XMaterial.BOOK)
                .name("${CC.GREEN}Open Party Commands")
                .addToLore(
                    "${CC.YELLOW}Click to run /party"
                )
                .toButton { _, _ ->
                    executeCommand(player, "party", "Party commands are currently unavailable.")
                }

            return
        }

        val leaderName = safeName(party.leaderId)

        buttons[20] = ItemBuilder
            .of(XMaterial.WRITABLE_BOOK)
            .name("${CC.GREEN}Party Overview")
            .addToLore(
                "${CC.GRAY}Leader: ${CC.WHITE}$leaderName",
                "${CC.GRAY}Members: ${CC.WHITE}${party.memberIds.size}${CC.GRAY}/${CC.WHITE}${party.limit}",
                "${CC.GRAY}Status: ${CC.WHITE}${party.status}",
                "${CC.GRAY}Party ID: ${CC.D_GRAY}${party.partyId}"
            )
            .toButton()

        buttons[21] = ItemBuilder
            .of(XMaterial.BOOK)
            .name("${CC.GREEN}Open Party Menu")
            .addToLore("${CC.YELLOW}Click to run /party")
            .toButton { _, _ ->
                executeCommand(player, "party", "Party commands are currently unavailable.")
            }

        buttons[22] = ItemBuilder
            .of(XMaterial.NAME_TAG)
            .name("${CC.GREEN}Invite Player")
            .addToLore(
                "${CC.GRAY}Invite someone directly from this menu.",
                "",
                "${CC.YELLOW}Click to type a username"
            )
            .toButton { _, _ ->
                promptAndRunCommand(
                    player,
                    "${CC.GREEN}Enter a player to invite to your party:",
                    "party invite %input%",
                    ProfileCategory.PARTY
                )
            }

        buttons[23] = ItemBuilder
            .of(XMaterial.COMPASS)
            .name("${CC.GREEN}Party Settings")
            .addToLore("${CC.YELLOW}Click to run /party settings")
            .toButton { _, _ ->
                executeCommand(player, "party settings", "Party settings are currently unavailable.")
            }

        buttons[24] = ItemBuilder
            .of(XMaterial.RED_DYE)
            .name("${CC.RED}Leave Party")
            .addToLore("${CC.YELLOW}Click to run /party leave")
            .toButton { _, _ ->
                executeCommand(player, "party leave", "Could not leave the party.")
            }

        for ((index, uuid) in party.memberIds.take(playerListSlots.size).withIndex())
        {
            val slot = playerListSlots[index]
            val name = safeName(uuid)
            val online = isOnline(uuid)

            buttons[slot] = playerHead(name)
                .name("${if (online) CC.GREEN else CC.RED}$name")
                .addToLore(
                    "${CC.GRAY}Status: ${if (online) "${CC.GREEN}Online" else "${CC.RED}Offline"}",
                    "${CC.GRAY}${if (uuid == party.leaderId) "Role: Leader" else "Role: Member"}"
                )
                .toButton()
        }
    }

    private fun populateGuildCategory(player: Player, buttons: MutableMap<Int, Button>)
    {
        val guild = loadGuild(player.uniqueId)

        buttons[18] = textureHead(greenWoolHeadTexture)
            .name("${CC.GREEN}Guild Category")
            .addToLore(
                "${CC.GRAY}View guild information, members,",
                "${CC.GRAY}and management actions."
            )
            .toButton()

        if (guild == null)
        {
            buttons[20] = ItemBuilder
                .of(XMaterial.BARRIER)
                .name("${CC.RED}You Are Not In A Guild")
                .addToLore(
                    "${CC.GRAY}Create or join a guild to",
                    "${CC.GRAY}see guild details here."
                )
                .toButton()

            buttons[21] = ItemBuilder
                .of(XMaterial.NAME_TAG)
                .name("${CC.GREEN}Create Guild")
                .addToLore(
                    "${CC.YELLOW}Click to type a guild name"
                )
                .toButton { _, _ ->
                    promptAndRunCommand(
                        player,
                        "${CC.GREEN}Enter your new guild name:",
                        "guild create %input%",
                        ProfileCategory.GUILD
                    )
                }

            buttons[22] = ItemBuilder
                .of(XMaterial.BOOK)
                .name("${CC.GREEN}Open Guild Commands")
                .addToLore("${CC.YELLOW}Click to run /guild")
                .toButton { _, _ ->
                    executeCommand(player, "guild", "Guild commands are currently unavailable.")
                }

            return
        }

        val memberIds = buildGuildMemberList(guild)
        val role = when (player.uniqueId)
        {
            guild.creator.uniqueId -> "Leader"
            else -> guild.members[player.uniqueId]?.role?.name ?: "Member"
        }

        buttons[20] = ItemBuilder
            .of(XMaterial.BOOK)
            .name("${CC.GREEN}${guild.name}")
            .addToLore(
                "${CC.GRAY}Role: ${CC.WHITE}$role",
                "${CC.GRAY}Members: ${CC.WHITE}${memberIds.size}",
                "${CC.GRAY}Tag: ${CC.WHITE}${guild.tag ?: "None"}",
                "${CC.GRAY}Created: ${CC.WHITE}${TimeUtil.formatIntoDateString(guild.createdOn.time)}"
            )
            .toButton()

        buttons[21] = ItemBuilder
            .of(XMaterial.BOOK)
            .name("${CC.GREEN}Open Guild Menu")
            .addToLore("${CC.YELLOW}Click to run /guild")
            .toButton { _, _ ->
                executeCommand(player, "guild", "Guild commands are currently unavailable.")
            }

        buttons[22] = ItemBuilder
            .of(XMaterial.NAME_TAG)
            .name("${CC.GREEN}Invite To Guild")
            .addToLore(
                "${CC.YELLOW}Click to type a username"
            )
            .toButton { _, _ ->
                promptAndRunCommand(
                    player,
                    "${CC.GREEN}Enter a player to invite to your guild:",
                    "guild invite %input%",
                    ProfileCategory.GUILD
                )
            }

        val estimatedLevel = max(1, (memberIds.size / 5) + 1)
        buttons[23] = ItemBuilder
            .of(XMaterial.EXPERIENCE_BOTTLE)
            .name("${CC.GREEN}Guild Level")
            .addToLore(
                "${CC.GRAY}Current level: ${CC.AQUA}$estimatedLevel",
                "${CC.D_GRAY}Based on current member size"
            )
            .toButton()

        val discordLink = extractDiscordLink(guild.description)
        buttons[24] = ItemBuilder
            .of(XMaterial.PAPER)
            .name("${CC.GREEN}Guild Discord")
            .addToLore(
                "${CC.GRAY}Link: ${CC.WHITE}${discordLink ?: "Not set"}",
                "",
                if (discordLink == null) "${CC.D_GRAY}Set one in guild description" else "${CC.YELLOW}Click to print in chat"
            )
            .toButton { _, _ ->
                if (discordLink == null)
                {
                    Button.playFail(player)
                    player.sendMessage("${CC.RED}No guild discord link is currently set.")
                    return@toButton
                }

                Button.playNeutral(player)
                player.sendMessage("${CC.GREEN}Guild Discord: ${CC.WHITE}$discordLink")
            }

        for ((index, uuid) in memberIds.take(playerListSlots.size).withIndex())
        {
            val slot = playerListSlots[index]
            val name = safeName(uuid)
            val online = isOnline(uuid)
            val isLeader = uuid == guild.creator.uniqueId

            buttons[slot] = playerHead(name)
                .name("${if (online) CC.GREEN else CC.RED}$name")
                .addToLore(
                    "${CC.GRAY}Role: ${CC.WHITE}${if (isLeader) "Leader" else guild.members[uuid]?.role?.name ?: "Member"}",
                    "${CC.GRAY}Status: ${if (online) "${CC.GREEN}Online" else "${CC.RED}Offline"}"
                )
                .toButton()
        }
    }

    private fun populateRecentPlayersCategory(player: Player, buttons: MutableMap<Int, Button>)
    {
        val recentPlayers = loadRecentPlayers(player.uniqueId)

        buttons[18] = textureHead(grayWoolHeadTexture)
            .name("${CC.GREEN}Recent Players Category")
            .addToLore(
                "${CC.GRAY}Players found from your recent",
                "${CC.GRAY}profile metadata entries."
            )
            .toButton()

        buttons[20] = ItemBuilder
            .of(XMaterial.PAPER)
            .name("${CC.GREEN}Recent Players Overview")
            .addToLore(
                "${CC.GRAY}Found: ${CC.WHITE}${recentPlayers.size} players",
                "",
                if (recentPlayers.isEmpty())
                    "${CC.D_GRAY}No recent players were found"
                else
                    "${CC.GREEN}Recent players listed below"
            )
            .toButton()

        buttons[21] = ItemBuilder
            .of(XMaterial.COMPASS)
            .name("${CC.GREEN}Open /recentplayers")
            .addToLore("${CC.YELLOW}Click to run command")
            .toButton { _, _ ->
                executeCommand(player, "recentplayers", "Recent players command is currently unavailable.")
            }

        buttons[22] = ItemBuilder
            .of(XMaterial.CLOCK)
            .name("${CC.GREEN}Refresh Category")
            .addToLore("${CC.YELLOW}Click to refresh list")
            .toButton { _, _ ->
                Button.playNeutral(player)
                ProfileMenu(ProfileCategory.RECENT).openMenu(player)
            }

        if (recentPlayers.isEmpty())
        {
            buttons[31] = ItemBuilder
                .of(XMaterial.BARRIER)
                .name("${CC.RED}No Recent Players Found")
                .addToLore(
                    "${CC.GRAY}Play a few matches and reopen",
                    "${CC.GRAY}this category to populate data."
                )
                .toButton()
            return
        }

        for ((index, uuid) in recentPlayers.take(playerListSlots.size).withIndex())
        {
            val slot = playerListSlots[index]
            val name = safeName(uuid)
            val online = isOnline(uuid)

            buttons[slot] = playerHead(name)
                .name("${if (online) CC.GREEN else CC.RED}$name")
                .addToLore(
                    "${CC.GRAY}Status: ${if (online) "${CC.GREEN}Online" else "${CC.RED}Offline"}",
                    "",
                    "${CC.YELLOW}Click to view profile"
                )
                .toButton { _, _ ->
                    executeCommand(
                        player,
                        "profile $name",
                        "Could not open profile for $name."
                    )
                }
        }
    }

    private fun executeCommand(
        player: Player,
        command: String,
        unavailableMessage: String
    )
    {
        if (player.performCommand(command))
        {
            Button.playNeutral(player)
            return
        }

        Button.playFail(player)
        player.sendMessage("${CC.RED}$unavailableMessage")
    }

    private fun openFriendRequestsMenu(player: Player)
    {
        val incoming = loadIncomingFriendRequests(player.uniqueId)
        val outgoing = loadOutgoingFriendRequests(player.uniqueId)

        object : Menu("Friend Requests")
        {
            override fun size(buttons: Map<Int, Button>) = 54

            override fun getButtons(player: Player): Map<Int, Button>
            {
                val buttons = mutableMapOf<Int, Button>()

                val pane = ItemBuilder
                    .of(Material.STAINED_GLASS_PANE)
                    .data(15)
                    .name("${CC.D_GRAY} ")
                    .toButton()

                for (slot in 0..8)
                {
                    buttons[slot] = pane
                }

                buttons[4] = ItemBuilder
                    .of(XMaterial.NETHER_STAR)
                    .name("${CC.GREEN}Friend Requests")
                    .addToLore(
                        "${CC.GRAY}Incoming: ${CC.YELLOW}${incoming.size}",
                        "${CC.GRAY}Outgoing: ${CC.YELLOW}${outgoing.size}"
                    )
                    .toButton()

                buttons[49] = ItemBuilder
                    .of(XMaterial.ARROW)
                    .name("${CC.YELLOW}Back")
                    .addToLore("${CC.YELLOW}Click to return")
                    .toButton { _, _ ->
                        ProfileMenu(ProfileCategory.FRIENDS).openMenu(player)
                    }

                var index = 0
                for ((uuid, expiresAt) in incoming.entries.sortedBy { it.value })
                {
                    if (index >= playerListSlots.size)
                    {
                        break
                    }

                    val slot = playerListSlots[index++]
                    val name = safeName(uuid)
                    val timeLeft = (expiresAt - System.currentTimeMillis()).coerceAtLeast(0L)

                    buttons[slot] = playerHead(name)
                        .name("${CC.GREEN}$name ${CC.GRAY}(Incoming)")
                        .addToLore(
                            "${CC.GRAY}Time left:",
                            "${CC.WHITE}${DurationFormatUtils.formatDurationWords(timeLeft, true, true)}",
                            "",
                            "${CC.YELLOW}Left-Click to accept",
                            "${CC.RED}Right-Click to ignore"
                        )
                        .toButton { _, click ->
                            if (click == null) return@toButton

                            if (click.isLeftClick)
                            {
                                executeCommand(
                                    player,
                                    "friend accept $name",
                                    "Could not accept request from $name."
                                )
                            } else if (click.isRightClick)
                            {
                                KFriendHandler.cacheConn.sync().hdel(
                                    "friends:requests:incoming:${player.uniqueId}",
                                    uuid.toString()
                                )
                                KFriendHandler.cacheConn.sync().hdel(
                                    "friends:requests:outgoing:$uuid",
                                    player.uniqueId.toString()
                                )
                                Button.playNeutral(player)
                            }

                            openFriendRequestsMenu(player)
                        }
                }

                for ((uuid, expiresAt) in outgoing.entries.sortedBy { it.value })
                {
                    if (index >= playerListSlots.size)
                    {
                        break
                    }

                    val slot = playerListSlots[index++]
                    val name = safeName(uuid)
                    val timeLeft = (expiresAt - System.currentTimeMillis()).coerceAtLeast(0L)

                    buttons[slot] = playerHead(name)
                        .name("${CC.GREEN}$name ${CC.GRAY}(Outgoing)")
                        .addToLore(
                            "${CC.GRAY}Time left:",
                            "${CC.WHITE}${DurationFormatUtils.formatDurationWords(timeLeft, true, true)}",
                            "",
                            "${CC.RED}Right-Click to cancel"
                        )
                        .toButton { _, click ->
                            if (click?.isRightClick == true)
                            {
                                KFriendHandler.cacheConn.sync().hdel(
                                    "friends:requests:outgoing:${player.uniqueId}",
                                    uuid.toString()
                                )
                                KFriendHandler.cacheConn.sync().hdel(
                                    "friends:requests:incoming:$uuid",
                                    player.uniqueId.toString()
                                )
                                Button.playNeutral(player)
                                openFriendRequestsMenu(player)
                            }
                        }
                }

                if (incoming.isEmpty() && outgoing.isEmpty())
                {
                    buttons[22] = ItemBuilder
                        .of(XMaterial.BARRIER)
                        .name("${CC.RED}No Pending Requests")
                        .addToLore(
                            "${CC.GRAY}You currently have no incoming",
                            "${CC.GRAY}or outgoing friend requests."
                        )
                        .toButton()
                }

                return buttons
            }
        }.openMenu(player)
    }

    private fun promptAndRunCommand(
        player: Player,
        promptText: String,
        commandTemplate: String,
        reopenCategory: ProfileCategory
    )
    {
        player.closeInventory()

        InputPrompt()
            .withText(promptText)
            .acceptInput { _, rawText ->
                val input = rawText.trim()
                if (input.isBlank())
                {
                    player.sendMessage("${CC.RED}Input cannot be empty.")
                    Tasks.sync {
                        ProfileMenu(reopenCategory).openMenu(player)
                    }
                    return@acceptInput
                }

                val command = commandTemplate.replace("%input%", input)
                if (!player.performCommand(command))
                {
                    Button.playFail(player)
                    player.sendMessage("${CC.RED}Could not run: ${CC.WHITE}/$command")
                } else
                {
                    Button.playNeutral(player)
                }

                Tasks.sync {
                    ProfileMenu(reopenCategory).openMenu(player)
                }
            }
            .start(player)
    }

    private fun loadFriendIds(player: Player): List<UUID>
    {
        val friendPlayer = KPlayerHandler.find(player)
            ?: return emptyList()

        val orderedRaw = runCatching {
            val method = friendPlayer.javaClass.methods
                .firstOrNull { it.name == "orderedFriends" && it.parameterCount == 0 }
                ?: return emptyList()

            method.invoke(friendPlayer)
        }.getOrElse {
            return emptyList()
        }

        val resolved = when (orderedRaw)
        {
            is CompletableFuture<*> -> runCatching { orderedRaw.join() }.getOrNull()
            else -> orderedRaw
        }

        val iterable = resolved as? Iterable<*>
            ?: return emptyList()

        return iterable
            .mapNotNull { entry ->
                when (entry)
                {
                    is UUID -> entry
                    null -> null
                    else -> runCatching {
                        val otherEnd = entry.javaClass.methods
                            .firstOrNull {
                                it.name == "otherEndOf" &&
                                    it.parameterCount == 1 &&
                                    it.parameterTypes[0] == UUID::class.java
                            }
                            ?: return@runCatching null

                        otherEnd.invoke(entry, player.uniqueId) as? UUID
                    }.getOrNull()
                }
            }
            .distinct()
            .sortedWith(
                compareByDescending<UUID> { isOnline(it) }
                    .thenBy { safeName(it).lowercase() }
            )
    }

    private fun loadIncomingFriendRequests(playerId: UUID): Map<UUID, Long>
    {
        return runCatching {
            val result = mutableMapOf<UUID, Long>()
            val raw = KFriendHandler.cacheConn
                .sync()
                .hgetall("friends:requests:incoming:$playerId")

            for ((key, value) in raw)
            {
                val requester = key.toUuidOrNull() ?: continue
                val expiresAt = value.toLongOrNull() ?: continue
                if (expiresAt <= System.currentTimeMillis())
                {
                    continue
                }

                result[requester] = expiresAt
            }

            result.toMap()
        }.getOrDefault(emptyMap())
    }

    private fun loadOutgoingFriendRequests(playerId: UUID): Map<UUID, Long>
    {
        return runCatching {
            val result = mutableMapOf<UUID, Long>()
            val raw = KFriendHandler.getOutgoingFriendRequests(playerId)

            for ((key, value) in raw)
            {
                val target = key.toUuidOrNull() ?: continue
                result[target] = value
            }

            result.toMap()
        }.getOrDefault(emptyMap())
    }

    private fun loadGuild(playerId: UUID): Guild?
    {
        return runCatching {
            GuildService.guildByUser(playerId).join()
        }.getOrNull()
    }

    private fun buildGuildMemberList(guild: Guild): List<UUID>
    {
        return buildList {
            add(guild.creator.uniqueId)
            addAll(guild.members.keys)
        }.distinct()
    }

    private fun loadPartySnapshot(playerId: UUID): PartySnapshot?
    {
        val rawParties = runCatching {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hgetall("minigames:parties")
        }.getOrElse {
            return null
        }

        for (rawParty in rawParties.values)
        {
            val json = runCatching {
                Serializers.gson.fromJson(rawParty, JsonObject::class.java)
            }.getOrNull() ?: continue

            val leader = json.getAsJsonObject("leader")
                ?.get("uniqueId")
                ?.asString
                ?.toUuidOrNull()
                ?: continue

            val memberIds = mutableListOf(leader)
            json.getAsJsonObject("members")
                ?.entrySet()
                ?.forEach { entry ->
                    entry.key.toUuidOrNull()?.let(memberIds::add)
                }

            if (playerId !in memberIds)
            {
                continue
            }

            val partyId = json.get("uniqueId")
                ?.asString
                ?.toUuidOrNull()
                ?: UUID.randomUUID()

            return PartySnapshot(
                partyId = partyId,
                leaderId = leader,
                memberIds = memberIds.distinct(),
                limit = json.get("limit")?.asInt ?: memberIds.size,
                status = json.get("status")?.asString ?: "PRIVATE"
            )
        }

        return null
    }

    private fun loadRecentPlayers(playerId: UUID): List<UUID>
    {
        val hash = runCatching {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hgetall("player:$playerId")
        }.getOrElse {
            return emptyList()
        }

        val players = linkedSetOf<UUID>()

        hash.entries
            .filter { it.key.contains("recent", ignoreCase = true) }
            .forEach { entry ->
                extractUuids(entry.value)
                    .filter { it != playerId }
                    .forEach(players::add)
            }

        return players.toList()
    }

    private fun extractUuids(raw: String): List<UUID>
    {
        val matches = uuidRegex
            .findAll(raw)
            .mapNotNull { it.value.toUuidOrNull() }
            .toMutableList()

        if (matches.isNotEmpty())
        {
            return matches.distinct()
        }

        raw.split(',', ';', ' ', '\n', '\t', '[', ']', '"')
            .forEach { token ->
                token.trim().toUuidOrNull()?.let(matches::add)
            }

        return matches.distinct()
    }

    private fun String.toUuidOrNull(): UUID?
    {
        return runCatching {
            UUID.fromString(this)
        }.getOrNull()
    }

    private fun safeName(uniqueId: UUID): String
    {
        return CubedCacheUtil.fetchName(uniqueId)
            ?: uniqueId.toString().substring(0, 8)
    }

    private fun isOnline(uniqueId: UUID): Boolean
    {
        return runCatching {
            QuickAccess.online(uniqueId).join()
        }.getOrDefault(false)
    }

    private fun buildProgressBar(percent: Float, segments: Int = 28): String
    {
        val clamped = percent.coerceIn(0.0f, 100.0f)
        val complete = ((clamped / 100.0f) * segments).toInt()

        return buildString {
            append(CC.AQUA)
            repeat(complete) {
                append("|")
            }

            append(CC.D_GRAY)
            repeat(segments - complete) {
                append("|")
            }
        }
    }

    private fun playerMetaLong(playerId: UUID, key: String): Long
    {
        return runCatching {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hget("player:$playerId", key)
                ?.replace(",", "")
                ?.toLongOrNull()
                ?: 0L
        }.getOrDefault(0L)
    }

    private fun extractDiscordLink(description: String?): String?
    {
        description ?: return null
        return discordRegex
            .find(description)
            ?.value
    }

    private fun readableError(throwable: Throwable): String
    {
        val root = generateSequence(throwable) { it.cause }
            .last()

        val message = root.message
            ?: "An unknown error occurred."

        return message
            .removePrefix("gg.scala.commons.acf.ConditionFailedException: ")
    }

    private fun textureHead(texture: String): ItemBuilder
    {
        return ItemBuilder
            .of(Material.SKULL_ITEM)
            .data(3)
            .setBase64Head(texture)
    }

    private fun playerHead(owner: String): ItemBuilder
    {
        return ItemBuilder
            .of(Material.SKULL_ITEM)
            .data(3)
            .owner(owner)
    }

    companion object
    {
        private val playerListSlots = listOf(
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
        )

        private val uuidRegex = Regex(
            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
        )

        private val discordRegex = Regex("""(https?://)?(www\.)?(discord\.gg|discord\.com/invite)/[A-Za-z0-9-]+""")

        private const val pinkWoolHeadTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA5NTA4NmE4NThmZmJkOGZiNmY1MGE1ZmUzYWQ2MzY4ZjNiYmI1NzJlYTc1N2Y3NjMzM2NiMzM1MDJmOGRjOCJ9fX0="
        private const val lightBlueWoolHeadTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzE4Njk5YmFlOWYxZTAyYTdhNjUxODNiZDAwNzk0ODllNGU2MTVkYWRkY2UzYzkyNjQ5Y2RlZDMxMmIyMjZkYyJ9fX0="
        private const val greenWoolHeadTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRlZDQxYjU4NTZiZjhiNWVmMDY2MGEyZmY2ZTA3ODA5NmQ5OTg1NzAxNjZiNzc2ZmQ2YjFiYTRjZDA5MDhmNCJ9fX0="
        private const val grayWoolHeadTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmY1NWE1NzYzOWM5ZjdkYmIyNDZiMjhhMjkyY2ZmMjk4MzQ0Yzk2OWNmZDQyNTI3MDlkNGE1ZmE2NjMxMjhiOSJ9fX0="
        private const val cyanWoolHeadTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQ5ZmVlNDA1MzIzMmFmZmU0YzdiZWMxMTc3MmFlM2JkYTQ0Y2JjZmFiZGJjMzFlM2QwYzA1MGY1MzNlZTA2YiJ9fX0="
        private const val globeHeadTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E1MTRiY2NhOGEwNDlhYWRlY2QxNDhhZTA0MzU3YWQwZDUzMzVkYjMwMDMzYTdiM2RkYjU3MWZkNjBkMTc1OCJ9fX0="
    }
}
