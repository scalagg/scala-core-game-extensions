package gg.scala.game.extensions.leveling.menu

import com.cryptomorin.xseries.XMaterial
import gg.scala.game.extensions.economy.EconomySyncService
import gg.scala.game.extensions.leveling.LevelSystem
import gg.scala.game.extensions.leveling.LevelSystemSyncService
import gg.scala.game.extensions.leveling.PrestigeType
import gg.scala.game.extensions.leveling.XpStrategy
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.Menu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import net.evilblock.cubed.util.bukkit.prompt.NumberPrompt
import org.bukkit.entity.Player

/**
 * @author Limo
 * @date 2/11/2026
 */
class ManageLevelSystemMenu(private val system: LevelSystem) : Menu(
    "Managing Level System ${system.id}"
)
{
    init
    {
        autoUpdate = true
        placeholder = true
    }

    override fun size(buttons: Map<Int, Button>) = 45

    override fun getButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        buttons[10] = buildPrettyNameButton(player)
        buttons[11] = buildXpStrategyButton(player)
        buttons[12] = buildPrestigeTypeButton(player)
        buttons[13] = buildCustomLevelFormatButton(player)
        buttons[14] = buildStartingLevelButton(player)
        buttons[15] = buildStartingXpButton(player)
        buttons[16] = buildMaxLevelButton(player)

        buttons[19] = buildCycleLengthButton(player)
        buttons[20] = buildEnablePrestigesButton(player)
        buttons[21] = buildEnableEventsButton(player)
        buttons[22] = buildEnableNegativeLevelsButton(player)
        buttons[23] = buildPrimaryGroupChatButton(player)
        buttons[24] = buildLinkedEconomyButton(player)

        addStrategySpecificButtons(buttons, player)

        return buttons
    }

    private fun buildPrettyNameButton(player: Player): Button
    {
        return ItemBuilder
            .of(XMaterial.EMERALD)
            .name("${CC.YELLOW}${CC.BOLD}Pretty Gamemode Name")
            .addToLore(
                "${CC.GRAY}Display name shown to players.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${system.prettyName}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter the display name for this level system")
                    .acceptInput { _, text ->
                        system.prettyName = text
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Updated pretty name to '$text'!")
                        Button.playSuccess(player)
                        ManageLevelSystemMenu(system).openMenu(player)
                    }
                    .start(player)
            }
    }

    private fun buildXpStrategyButton(player: Player): Button
    {
        val lore = mutableListOf<String>()
        lore.add("${CC.GRAY}Determines XP scaling per level.")
        lore.add("")

        for (strategy in XpStrategy.entries)
        {
            val arrow = if (strategy == system.xpStrategy) "${CC.GREEN}\u25ba" else "${CC.GRAY}\u25ba"
            lore.add("$arrow ${CC.WHITE}${strategy.name}")
        }

        lore.add("")
        lore.add("${CC.YELLOW}Left-Click to scroll down!")
        lore.add("${CC.YELLOW}Right-Click to scroll up!")

        return ItemBuilder
            .of(XMaterial.EXPERIENCE_BOTTLE)
            .name("${CC.YELLOW}${CC.BOLD}XP Strategy")
            .setLore(lore)
            .toButton { _, type ->
                system.xpStrategy = if (type!!.isRightClick)
                    system.xpStrategy.previous()
                else
                    system.xpStrategy.next()

                LevelSystemSyncService.update(system)
                Button.playNeutral(player)
                ManageLevelSystemMenu(system).openMenu(player)
            }
    }

    private fun buildPrestigeTypeButton(player: Player): Button
    {
        val lore = mutableListOf<String>()
        lore.add("${CC.GRAY}Color/symbol progression style.")
        lore.add("")

        for (prestige in PrestigeType.entries)
        {
            val arrow = if (prestige == system.prestigeType) "${CC.GREEN}\u25ba" else "${CC.GRAY}\u25ba"
            lore.add("$arrow ${CC.WHITE}${prestige.name}")
        }

        lore.add("")
        lore.add("${CC.YELLOW}Left-Click to scroll down!")
        lore.add("${CC.YELLOW}Right-Click to scroll up!")

        return ItemBuilder
            .of(XMaterial.DIAMOND)
            .name("${CC.YELLOW}${CC.BOLD}Prestige Type")
            .setLore(lore)
            .toButton { _, type ->
                system.prestigeType = if (type!!.isRightClick)
                    system.prestigeType.previous()
                else
                    system.prestigeType.next()

                LevelSystemSyncService.update(system)
                Button.playNeutral(player)
                ManageLevelSystemMenu(system).openMenu(player)
            }
    }

    private fun buildCustomLevelFormatButton(player: Player): Button
    {
        return ItemBuilder
            .of(XMaterial.NAME_TAG)
            .name("${CC.YELLOW}${CC.BOLD}Custom Level Format")
            .addToLore(
                "${CC.GRAY}Format string for level display.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${system.levelFormat}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter the level format ${CC.GRAY}(e.g. {color}[{level}{symbol}{color}])")
                    .acceptInput { _, text ->
                        system.levelFormat = text
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Updated level format to '$text'!")
                        Button.playSuccess(player)
                        ManageLevelSystemMenu(system).openMenu(player)
                    }
                    .start(player)
            }
    }

    private fun buildStartingLevelButton(player: Player): Button
    {
        return ItemBuilder
            .of(XMaterial.GREEN_WOOL)
            .name("${CC.YELLOW}${CC.BOLD}Starting Level")
            .addToLore(
                "${CC.GRAY}Level players begin at.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${system.startingLevel}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                NumberPrompt()
                    .withText("Enter the starting level")
                    .acceptInput { number ->
                        system.startingLevel = number.toInt()
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Updated starting level to ${number.toInt()}!")
                        Button.playSuccess(player)
                        ManageLevelSystemMenu(system).openMenu(player)
                    }
                    .start(player)
            }
    }

    private fun buildStartingXpButton(player: Player): Button
    {
        return ItemBuilder
            .of(XMaterial.LIME_WOOL)
            .name("${CC.YELLOW}${CC.BOLD}Starting XP")
            .addToLore(
                "${CC.GRAY}XP players begin with.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${system.startingXp}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                NumberPrompt()
                    .withText("Enter the starting XP")
                    .acceptInput { number ->
                        system.startingXp = number.toLong()
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Updated starting XP to ${number.toLong()}!")
                        Button.playSuccess(player)
                        ManageLevelSystemMenu(system).openMenu(player)
                    }
                    .start(player)
            }
    }

    private fun buildMaxLevelButton(player: Player): Button
    {
        val displayMax = if (system.maxLevel >= 999999) "Unlimited" else system.maxLevel.toString()

        return ItemBuilder
            .of(XMaterial.RED_WOOL)
            .name("${CC.YELLOW}${CC.BOLD}Max Level")
            .addToLore(
                "${CC.GRAY}Highest level a player can reach.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}$displayMax",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                NumberPrompt()
                    .withText("Enter the max level ${CC.GRAY}(999999 for unlimited)")
                    .acceptInput { number ->
                        system.maxLevel = number.toInt()
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Updated max level to ${number.toInt()}!")
                        Button.playSuccess(player)
                        ManageLevelSystemMenu(system).openMenu(player)
                    }
                    .start(player)
            }
    }

    private fun buildCycleLengthButton(player: Player): Button
    {
        val displayCycle = if (system.cycleLength > 0) "${system.cycleLength} levels" else "Disabled"

        return ItemBuilder
            .of(XMaterial.FEATHER)
            .name("${CC.YELLOW}${CC.BOLD}Cycle Length")
            .addToLore(
                "${CC.GRAY}Repeats prestige colors every N levels.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}$displayCycle",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                NumberPrompt()
                    .withText("Enter the cycle length ${CC.GRAY}(0 to disable)")
                    .acceptInput { number ->
                        system.cycleLength = number.toInt()
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Updated cycle length to ${number.toInt()}!")
                        Button.playSuccess(player)
                        ManageLevelSystemMenu(system).openMenu(player)
                    }
                    .start(player)
            }
    }

    private fun buildEnablePrestigesButton(player: Player): Button
    {
        val enabled = system.prestigesEnabled

        return ItemBuilder
            .of(XMaterial.DIAMOND_BLOCK)
            .name("${CC.YELLOW}${CC.BOLD}Enable Prestiges")
            .addToLore(
                "${CC.GRAY}Allow prestige progression.",
                "",
                "${CC.GRAY}Status: ${if (enabled) "${CC.GREEN}Enabled" else "${CC.RED}Disabled"}",
                "",
                "${CC.YELLOW}Click to toggle!"
            )
            .toButton { _, _ ->
                system.prestigesEnabled = !system.prestigesEnabled
                LevelSystemSyncService.update(system)
                Button.playNeutral(player)
                ManageLevelSystemMenu(system).openMenu(player)
            }
    }

    private fun buildEnableEventsButton(player: Player): Button
    {
        val enabled = system.eventsEnabled

        return ItemBuilder
            .of(XMaterial.OAK_SIGN)
            .name("${CC.YELLOW}${CC.BOLD}Enable Events")
            .addToLore(
                "${CC.GRAY}Fire level-up and prestige events.",
                "",
                "${CC.GRAY}Status: ${if (enabled) "${CC.GREEN}Enabled" else "${CC.RED}Disabled"}",
                "",
                "${CC.YELLOW}Click to toggle!"
            )
            .toButton { _, _ ->
                system.eventsEnabled = !system.eventsEnabled
                LevelSystemSyncService.update(system)
                Button.playNeutral(player)
                ManageLevelSystemMenu(system).openMenu(player)
            }
    }

    private fun buildEnableNegativeLevelsButton(player: Player): Button
    {
        val enabled = system.negativeLevelsEnabled

        return ItemBuilder
            .of(XMaterial.BARRIER)
            .name("${CC.YELLOW}${CC.BOLD}Negative Levels")
            .addToLore(
                "${CC.GRAY}Allow levels below zero.",
                "",
                "${CC.GRAY}Status: ${if (enabled) "${CC.GREEN}Enabled" else "${CC.RED}Disabled"}",
                "",
                "${CC.YELLOW}Click to toggle!"
            )
            .toButton { _, _ ->
                system.negativeLevelsEnabled = !system.negativeLevelsEnabled
                LevelSystemSyncService.update(system)
                Button.playNeutral(player)
                ManageLevelSystemMenu(system).openMenu(player)
            }
    }

    private fun buildPrimaryGroupChatButton(player: Player): Button
    {
        val lore = mutableListOf<String>()
        lore.add("${CC.GRAY}Server groups using this level prefix.")
        lore.add("")

        if (system.primaryGroupChatOverrides.isEmpty())
        {
            lore.add("${CC.GRAY}None configured")
        }
        else
        {
            for ((index, group) in system.primaryGroupChatOverrides.withIndex())
            {
                lore.add("${CC.GRAY}${index + 1}. ${CC.WHITE}$group")
            }
        }

        lore.add("")
        lore.add("${CC.YELLOW}Left-Click to add!")
        lore.add("${CC.YELLOW}Right-Click to clear!")

        return ItemBuilder
            .of(XMaterial.PAPER)
            .name("${CC.YELLOW}${CC.BOLD}Primary Group Chat Override")
            .setLore(lore)
            .toButton { _, type ->
                if (type!!.isRightClick)
                {
                    system.primaryGroupChatOverrides.clear()
                    LevelSystemSyncService.update(system)
                    Button.playSuccess(player)
                    player.sendMessage("${CC.GREEN}Cleared all group chat overrides!")
                    ManageLevelSystemMenu(system).openMenu(player)
                    return@toButton
                }

                player.closeInventory()
                Button.playNeutral(player)

                InputPrompt()
                    .withText("Enter a server group to add ${CC.GRAY}(e.g. bwlobby)")
                    .acceptInput { _, text ->
                        system.primaryGroupChatOverrides.add(text)
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Added group '$text'!")
                        Button.playSuccess(player)
                        ManageLevelSystemMenu(system).openMenu(player)
                    }
                    .start(player)
            }
    }

    private fun buildLinkedEconomyButton(player: Player): Button
    {
        val allEconomies = EconomySyncService.all()
        val economyEntries = allEconomies.entries.toList()
        val lore = mutableListOf<String>()
        lore.add("${CC.GRAY}Economy awarded on level-up.")
        lore.add("")

        for ((id, economyData) in economyEntries)
        {
            val selected = system.linkedEconomy == id
            val arrow = if (selected) "${CC.GREEN}\u25ba" else "${CC.GRAY}\u25ba"
            lore.add("$arrow ${CC.WHITE}${economyData.resolvedDisplayName()}")
        }

        val noneSelected = system.linkedEconomy == null
        val noneArrow = if (noneSelected) "${CC.GREEN}\u25ba" else "${CC.GRAY}\u25ba"
        lore.add("$noneArrow ${CC.WHITE}None")

        lore.add("")
        lore.add("${CC.YELLOW}Left-Click to scroll down!")
        lore.add("${CC.YELLOW}Right-Click to scroll up!")

        return ItemBuilder
            .of(XMaterial.LAPIS_LAZULI)
            .name("${CC.YELLOW}${CC.BOLD}Linked Economy")
            .setLore(lore)
            .toButton { _, type ->
                val options = mutableListOf<String?>()
                for ((id, _) in economyEntries)
                {
                    options.add(id)
                }
                options.add(null)

                if (options.isEmpty()) return@toButton

                val currentIndex = options.indexOfFirst { it == system.linkedEconomy }
                    .let { if (it == -1) options.size - 1 else it }

                val nextIndex = if (type!!.isRightClick)
                    (currentIndex - 1 + options.size) % options.size
                else
                    (currentIndex + 1) % options.size

                system.linkedEconomy = options[nextIndex]
                LevelSystemSyncService.update(system)
                Button.playNeutral(player)
                ManageLevelSystemMenu(system).openMenu(player)
            }
    }

    private fun addStrategySpecificButtons(buttons: MutableMap<Int, Button>, player: Player)
    {
        var nextSlot = 28

        when (system.xpStrategy)
        {
            XpStrategy.LINEAR ->
            {
                buttons[nextSlot++] = ItemBuilder
                    .of(XMaterial.GOLDEN_APPLE)
                    .name("${CC.YELLOW}${CC.BOLD}Linear XP Per Level")
                    .addToLore(
                        "${CC.GRAY}Flat XP required per level.",
                        "",
                        "${CC.GRAY}Current: ${CC.WHITE}${system.linearXpPerLevel}",
                        "",
                        "${CC.YELLOW}Click to edit!"
                    )
                    .toButton { _, _ ->
                        player.closeInventory()
                        Button.playNeutral(player)

                        NumberPrompt()
                            .withText("Enter the XP required per level")
                            .acceptInput { number ->
                                system.linearXpPerLevel = number.toLong()
                                LevelSystemSyncService.update(system)

                                player.sendMessage("${CC.GREEN}Updated linear XP per level to ${number.toLong()}!")
                                Button.playSuccess(player)
                                ManageLevelSystemMenu(system).openMenu(player)
                            }
                            .start(player)
                    }
            }
            XpStrategy.EXPONENTIAL ->
            {
                buttons[nextSlot++] = ItemBuilder
                    .of(XMaterial.GOLDEN_APPLE)
                    .name("${CC.YELLOW}${CC.BOLD}Exponential Base XP")
                    .addToLore(
                        "${CC.GRAY}Base XP before multiplier.",
                        "",
                        "${CC.GRAY}Current: ${CC.WHITE}${system.exponentialBaseXp}",
                        "",
                        "${CC.YELLOW}Click to edit!"
                    )
                    .toButton { _, _ ->
                        player.closeInventory()
                        Button.playNeutral(player)

                        NumberPrompt()
                            .withText("Enter the base XP")
                            .acceptInput { number ->
                                system.exponentialBaseXp = number.toLong()
                                LevelSystemSyncService.update(system)

                                player.sendMessage("${CC.GREEN}Updated exponential base XP to ${number.toLong()}!")
                                Button.playSuccess(player)
                                ManageLevelSystemMenu(system).openMenu(player)
                            }
                            .start(player)
                    }

                buttons[nextSlot++] = ItemBuilder
                    .of(XMaterial.BLAZE_POWDER)
                    .name("${CC.YELLOW}${CC.BOLD}Exponential Multiplier")
                    .addToLore(
                        "${CC.GRAY}XP scaling factor per level.",
                        "",
                        "${CC.GRAY}Current: ${CC.WHITE}${system.exponentialMultiplier}",
                        "",
                        "${CC.YELLOW}Click to edit!"
                    )
                    .toButton { _, _ ->
                        player.closeInventory()
                        Button.playNeutral(player)

                        NumberPrompt()
                            .withText("Enter the multiplier ${CC.GRAY}(e.g. 1.1)")
                            .acceptInput { number ->
                                system.exponentialMultiplier = number.toDouble()
                                LevelSystemSyncService.update(system)

                                player.sendMessage("${CC.GREEN}Updated exponential multiplier to ${number.toDouble()}!")
                                Button.playSuccess(player)
                                ManageLevelSystemMenu(system).openMenu(player)
                            }
                            .start(player)
                    }
            }
            XpStrategy.RANGE_BASED ->
            {
                buttons[nextSlot++] = ItemBuilder
                    .of(XMaterial.BOOK)
                    .name("${CC.YELLOW}${CC.BOLD}Manage XP Ranges")
                    .addToLore(
                        "${CC.GRAY}Level ranges with custom XP.",
                        "",
                        "${CC.GRAY}Ranges: ${CC.WHITE}${system.xpRanges.size}",
                        "",
                        "${CC.YELLOW}Click to manage!"
                    )
                    .toButton { _, _ ->
                        Button.playNeutral(player)
                        ManageXpRangesMenu(system).openMenu(player)
                    }
            }
            XpStrategy.POLYNOMIAL ->
            {
                val coeffLore = mutableListOf<String>()
                coeffLore.add("${CC.GRAY}Polynomial: sum of coeff[i] * level^i")
                coeffLore.add("")
                for ((index, coeff) in system.polynomialCoefficients.withIndex())
                {
                    coeffLore.add("${CC.GRAY}${index + 1}. ${CC.WHITE}$coeff")
                }
                coeffLore.add("")
                coeffLore.add("${CC.YELLOW}Click to edit!")

                buttons[nextSlot++] = ItemBuilder
                    .of(XMaterial.BOOK)
                    .name("${CC.YELLOW}${CC.BOLD}Polynomial Coefficients")
                    .setLore(coeffLore)
                    .toButton { _, _ ->
                        player.closeInventory()
                        Button.playNeutral(player)

                        InputPrompt()
                            .withText("Enter coefficients ${CC.GRAY}(comma-separated, e.g. 100,50,10)")
                            .acceptInput { _, text ->
                                try
                                {
                                    val coefficients = text.split(",")
                                        .map { it.trim().toDouble() }
                                        .toMutableList()
                                    system.polynomialCoefficients = coefficients
                                    LevelSystemSyncService.update(system)

                                    player.sendMessage("${CC.GREEN}Updated polynomial coefficients to $coefficients!")
                                    Button.playSuccess(player)
                                }
                                catch (e: Exception)
                                {
                                    player.sendMessage("${CC.RED}Invalid format! Use comma-separated numbers.")
                                    Button.playFail(player)
                                }
                                ManageLevelSystemMenu(system).openMenu(player)
                            }
                            .start(player)
                    }
            }
            XpStrategy.BEDWARS_HYPIXEL ->
            {

            }
        }

        buttons[nextSlot] = ItemBuilder
            .of(XMaterial.REDSTONE)
            .name("${CC.YELLOW}${CC.BOLD}Min XP Per Level")
            .addToLore(
                "${CC.GRAY}Minimum XP floor for any level.",
                "",
                "${CC.GRAY}Current: ${CC.WHITE}${system.minXpPerLevel}",
                "",
                "${CC.YELLOW}Click to edit!"
            )
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                NumberPrompt()
                    .withText("Enter the minimum XP per level")
                    .acceptInput { number ->
                        system.minXpPerLevel = number.toLong()
                        LevelSystemSyncService.update(system)

                        player.sendMessage("${CC.GREEN}Updated min XP per level to ${number.toLong()}!")
                        Button.playSuccess(player)
                        ManageLevelSystemMenu(system).openMenu(player)
                    }
                    .start(player)
            }
    }

    override fun onClose(player: Player, manualClose: Boolean)
    {
        if (manualClose)
        {
            Tasks.sync {
                ManageLevelSystemsMenu().openMenu(player)
            }
        }
    }
}
