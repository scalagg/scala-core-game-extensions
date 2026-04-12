package gg.scala.game.extensions.economy

import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Limo
 * @date 2/5/2026
 */
object EconomyProfileService
{
    private val profiles = ConcurrentHashMap<UUID, EconomyProfile>()

    fun find(player: Player): EconomyProfile?
    {
        return profiles[player.uniqueId]
    }

    fun find(player: UUID): EconomyProfile?
    {
        return profiles[player]
    }

    fun loadCopyOf(player: UUID): CompletableFuture<EconomyProfile?>
    {
        return CompletableFuture.completedFuture(
            profiles.getOrPut(player) { EconomyProfile(player) }
        )
    }

    fun save(profile: EconomyProfile): CompletableFuture<Void>
    {
        profiles[profile.uniqueId] = profile
        return CompletableFuture.completedFuture(null)
    }

    fun clear(player: UUID)
    {
        profiles.remove(player)
    }
}
