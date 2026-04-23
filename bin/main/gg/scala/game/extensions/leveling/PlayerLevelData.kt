package gg.scala.game.extensions.leveling

/**
 * Per-player, per-level-system data tracking level progression.
 *
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
    /**
     * Adds experience and checks for level-up using the provided level system.
     * Returns the number of levels gained if any.
     */
    fun addExperience(amount: Long, system: LevelSystem): Int
    {
        if (amount <= 0) return 0

        experience += amount
        return crossCheckLevel(system)
    }

    /**
     * Recalculates level based on current XP and the level system config.
     * Returns the number of levels gained.
     */
    fun crossCheckLevel(system: LevelSystem): Int
    {
        val previousLevel = level
        var totalXp = 0L

        // Walk through levels to find where our XP puts us
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

    /**
     * Gets the XP progress within the current level.
     */
    fun getXpInCurrentLevel(system: LevelSystem): Long
    {
        var totalXp = 0L
        for (lvl in system.startingLevel until level)
        {
            totalXp += system.getXpForLevel(lvl + 1)
        }
        return experience - totalXp
    }

    /**
     * Gets the XP required to reach the next level from current.
     */
    fun getXpForNextLevel(system: LevelSystem): Long
    {
        if (level >= system.maxLevel) return 0L
        return system.getXpForLevel(level + 1)
    }

    /**
     * Gets the fraction of progress to the next level (0.0 to 1.0).
     */
    fun getProgressFraction(system: LevelSystem): Float
    {
        val required = getXpForNextLevel(system)
        if (required <= 0) return 1.0f
        val current = getXpInCurrentLevel(system)
        return (current.toFloat() / required.toFloat()).coerceIn(0.0f, 1.0f)
    }

    /**
     * Gets the percentage of progress to the next level (0.0 to 100.0).
     */
    fun getProgressPercentage(system: LevelSystem): Float
    {
        return getProgressFraction(system) * 100.0f
    }
}
