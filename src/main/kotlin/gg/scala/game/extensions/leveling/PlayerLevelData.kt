package gg.scala.game.extensions.leveling

/**
 * @author Limo
 * @date 2/11/2026
 */
data class PlayerLevelData(
    val levelSystemId: String,
    var level: Int = 0,
    var experience: Long = 0L,
    var prestige: Int = 0
)
{

    fun addExperience(amount: Long, system: LevelSystem): Int
    {
        if (amount <= 0) return 0

        experience += amount
        return crossCheckLevel(system)
    }

    fun crossCheckLevel(system: LevelSystem): Int
    {
        val previousLevel = level
        var totalXp = 0L

        var currentLevel = system.startingLevel
        while (currentLevel < system.maxLevel)
        {
            val required = system.getXpForLevel(currentLevel + 1)
            if (totalXp + required > experience) break
            totalXp += required
            currentLevel++
        }

        level = currentLevel
        return level - previousLevel
    }

    fun getXpInCurrentLevel(system: LevelSystem): Long
    {
        var totalXp = 0L
        for (lvl in system.startingLevel until level)
        {
            totalXp += system.getXpForLevel(lvl + 1)
        }
        return experience - totalXp
    }

    fun getXpForNextLevel(system: LevelSystem): Long
    {
        if (level >= system.maxLevel) return 0L
        return system.getXpForLevel(level + 1)
    }

    fun getProgressFraction(system: LevelSystem): Float
    {
        val required = getXpForNextLevel(system)
        if (required <= 0) return 1.0f
        val current = getXpInCurrentLevel(system)
        return (current.toFloat() / required.toFloat()).coerceIn(0.0f, 1.0f)
    }

    fun getProgressPercentage(system: LevelSystem): Float
    {
        return getProgressFraction(system) * 100.0f
    }
}
