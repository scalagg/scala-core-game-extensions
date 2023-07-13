package gg.tropic.game.extensions.guilds

import com.mongodb.client.model.Filters
import gg.scala.flavor.service.Service
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.type.DataStoreStorageType
import org.bson.conversions.Bson
import java.util.UUID

/**
 * @author GrowlyX
 * @since 7/13/2023
 */
@Service
object GuildService
{
    private fun findSingleWithFilter(filter: Bson) =
        DataStoreObjectControllerCache
            .findNotNull<Guild>()
            .mongo()
            .loadWithFilter(filter)

    fun destroy(guild: Guild) = DataStoreObjectControllerCache
        .findNotNull<Guild>()
        .delete(
            guild.identifier,
            DataStoreStorageType.MONGO
        )

    fun guildByName(name: String) =
        findSingleWithFilter(
            Filters.eq("name", name)
        )

    fun guildById(id: UUID) =
        findSingleWithFilter(
            Filters.eq("identifier", id.toString())
        )

    fun guildByUser(user: UUID) =
        findSingleWithFilter(
            Filters.or(
                Filters.eq("creator.uniqueId", user.toString()),
                Filters.exists("members.$user")
            )
        )
}
