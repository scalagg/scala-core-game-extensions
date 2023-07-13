package gg.tropic.game.extensions.guilds

import gg.scala.commons.annotations.Model
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.storable.IDataStoreObject
import gg.scala.store.storage.type.DataStoreStorageType
import java.util.*

/**
 * @author GrowlyX
 * @since 7/13/2023
 */
@Model
data class Guild(
    override val identifier: UUID =
        UUID.randomUUID(),
    val name: String,
    var creator: GuildMember,
    var createdOn: Date = Date(),
    var members: MutableMap<UUID, GuildMember> = mutableMapOf(),
    var description: String? = null,
    var outgoingInvitations: Map<UUID, Long> = mutableMapOf()
) : IDataStoreObject
{
    fun updateDescription(newDescription: String) =
        run {
            description = newDescription
            save()
        }

    fun removeMember(uniqueId: UUID) =
        run {
            members.remove(uniqueId)
            save()
        }

    fun addMember(uniqueId: UUID) =
        run {
            members[uniqueId] = GuildMember(uniqueId)
            save()
        }

    fun transferTo(member: GuildMember) =
        run {
            assert(members.containsValue(member))
            assert(member.uniqueId != creator.uniqueId)

            val previousCreator = creator
            members.remove(member.uniqueId)
            creator = member

            previousCreator.role = GuildRole.Member
            members[previousCreator.uniqueId] = previousCreator

            save()
        }

    fun save() = DataStoreObjectControllerCache
        .findNotNull<Guild>()
        .save(
            this,
            DataStoreStorageType.MONGO
        )
    // TODO: no filtering on cache layer, so what do we do with this?
        /*.thenCompose {
            DataStoreObjectControllerCache
                .findNotNull<Guild>()
                .save(
                    this,
                    DataStoreStorageType.REDIS
                )
        }*/
}
