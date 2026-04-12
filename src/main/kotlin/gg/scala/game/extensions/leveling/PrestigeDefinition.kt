package gg.scala.game.extensions.leveling

import net.evilblock.cubed.util.CC

/**
 * @author Limo
 * @date 2/11/2026
 */
data class PrestigeTier(
    val name: String,
    val color: String,
    val symbol: String = "✪",
    val requiredLevel: Int = 0
)

/**
 * @author Limo
 * @date 2/11/2026
 */
data class PrestigeDefinition(
    val type: PrestigeType,
    val tiers: List<PrestigeTier> = emptyList()
)
{

    fun tierForLevel(level: Int): PrestigeTier
    {
        return tiers.lastOrNull { level >= it.requiredLevel }
            ?: tiers.firstOrNull()
            ?: PrestigeTier("Default", CC.GRAY)
    }

    fun colorForLevel(level: Int): String
    {
        return tierForLevel(level).color
    }

    fun symbolForLevel(level: Int): String
    {
        return tierForLevel(level).symbol
    }

    companion object
    {

        fun bedwars(): PrestigeDefinition
        {
            return PrestigeDefinition(
                type = PrestigeType.BEDWARS,
                tiers = listOf(
                    PrestigeTier("Stone", CC.GRAY, "✪", 0),
                    PrestigeTier("Iron", CC.WHITE, "✪", 100),
                    PrestigeTier("Gold", CC.GOLD, "✪", 200),
                    PrestigeTier("Diamond", CC.AQUA, "✪", 300),
                    PrestigeTier("Emerald", CC.D_GREEN, "✪", 400),
                    PrestigeTier("Sapphire", CC.D_AQUA, "✪", 500),
                    PrestigeTier("Ruby", CC.D_RED, "✪", 600),
                    PrestigeTier("Crystal", CC.PINK, "✪", 700),
                    PrestigeTier("Opal", CC.BLUE, "✪", 800),
                    PrestigeTier("Amethyst", CC.D_PURPLE, "✪", 900),
                    PrestigeTier("Mirror", "${CC.WHITE}${CC.BOLD}", "✪", 1000),
                    PrestigeTier("Light", "${CC.YELLOW}${CC.BOLD}", "✪", 1100),
                    PrestigeTier("Dawn", "${CC.AQUA}${CC.BOLD}", "✪", 1200),
                    PrestigeTier("Dusk", "${CC.D_AQUA}${CC.BOLD}", "✪", 1300),
                    PrestigeTier("Air", "${CC.D_GREEN}${CC.BOLD}", "✪", 1400),
                    PrestigeTier("Wind", "${CC.GREEN}${CC.BOLD}", "✪", 1500),
                    PrestigeTier("Nebula", "${CC.D_RED}${CC.BOLD}", "✪", 1600),
                    PrestigeTier("Thunder", "${CC.YELLOW}${CC.BOLD}", "✪", 1700),
                    PrestigeTier("Dragon", "${CC.D_PURPLE}${CC.BOLD}", "✪", 1800),
                    PrestigeTier("Fairy", "${CC.PINK}${CC.BOLD}", "✪", 1900),
                    PrestigeTier("Eternal", "${CC.D_AQUA}${CC.BOLD}", "✪", 2000)
                )
            )
        }

        fun skywars(): PrestigeDefinition
        {
            return PrestigeDefinition(
                type = PrestigeType.SKYWARS,
                tiers = listOf(
                    PrestigeTier("Iron", CC.WHITE, "⋆", 0),
                    PrestigeTier("Gold", CC.GOLD, "⋆", 5),
                    PrestigeTier("Diamond", CC.AQUA, "⋆", 10),
                    PrestigeTier("Emerald", CC.D_GREEN, "⋆", 15),
                    PrestigeTier("Sapphire", CC.D_AQUA, "⋆", 20),
                    PrestigeTier("Ruby", CC.D_RED, "⋆", 25),
                    PrestigeTier("Crystal", CC.PINK, "⋆", 30),
                    PrestigeTier("Opal", CC.BLUE, "⋆", 35),
                    PrestigeTier("Amethyst", CC.D_PURPLE, "⋆", 40),
                    PrestigeTier("Rainbow", CC.RED, "⋆", 50)
                )
            )
        }

        fun duels(): PrestigeDefinition
        {
            return PrestigeDefinition(
                type = PrestigeType.DUELS,
                tiers = listOf(
                    PrestigeTier("Rookie", CC.GRAY, "✦", 0),
                    PrestigeTier("Iron", CC.WHITE, "✦", 50),
                    PrestigeTier("Gold", CC.GOLD, "✦", 100),
                    PrestigeTier("Diamond", CC.AQUA, "✦", 200),
                    PrestigeTier("Master", CC.D_GREEN, "✦", 500),
                    PrestigeTier("Legend", CC.D_RED, "✦", 1000),
                    PrestigeTier("Grandmaster", CC.YELLOW, "✦", 2000)
                )
            )
        }

        fun custom(): PrestigeDefinition
        {
            return PrestigeDefinition(
                type = PrestigeType.CUSTOM,
                tiers = listOf(
                    PrestigeTier("Default", CC.GRAY, "✪", 0)
                )
            )
        }

        fun forType(type: PrestigeType): PrestigeDefinition
        {
            return when (type)
            {
                PrestigeType.BEDWARS -> bedwars()
                PrestigeType.SKYWARS -> skywars()
                PrestigeType.DUELS -> duels()
                PrestigeType.CUSTOM -> custom()
            }
        }
    }
}
