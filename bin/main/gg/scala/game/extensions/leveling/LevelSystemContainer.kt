package gg.scala.game.extensions.leveling

/**
 * Container holding all registered level systems.
 * Stored as a single document in MongoDB and synced via Redis.
 *
 * @author Limo
 * @date 2/11/2026
 */
class LevelSystemContainer(
    val levelSystems: MutableMap<String, LevelSystem> = mutableMapOf()
)
