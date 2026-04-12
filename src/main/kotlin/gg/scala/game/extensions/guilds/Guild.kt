package gg.scala.game.extensions.guilds

import gg.scala.commons.annotations.Model
import gg.scala.lemon.util.QuickAccess.sendGlobalPlayerMessage
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.storable.IDataStoreObject
import gg.scala.store.storage.type.DataStoreStorageType
import java.time.Duration
import java.util.*
import java.util.concurrent.CompletableFuture

@Model
data class Guild(
    override val identifier: UUID =
        UUID.randomUUID(),
    val name: String,
    val id: Int = GuildIds.incrementGuid(),
    var tag: String? = null,
    var creator: GuildMember,
    var createdOn: Date = Date(),
    var members: MutableMap<UUID, GuildMember> = mutableMapOf(),
    var description: String? = null,
    var allInvite: Boolean = false,
    var outgoingInvitations: MutableMap<UUID, Long> = mutableMapOf()
) : IDataStoreObject
{
    fun member(uniqueId: UUID) = if (creator.uniqueId == uniqueId)
        creator else members[uniqueId]!!

    fun sendMessage(message: String) = CompletableFuture
        .allOf(
            sendGlobalPlayerMessage("$prefix$message", creator.uniqueId),
            *members.keys
                .map {
                    sendGlobalPlayerMessage("$prefix$message", it)
                }
                .toTypedArray()
        )

    fun updateDescription(newDescription: String) =
        run {
            description = newDescription
            save()
        }

    fun updateAllInvite() =
        run {
            allInvite = !allInvite
            save()
        }

    fun setTag(guildTag: String?) =
        run {
            tag = guildTag
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

    fun addOutgoingInvite(user: UUID) =
        run {
            outgoingInvitations[user] =
                System.currentTimeMillis() + Duration.ofMinutes(5).toMillis()
            save()
        }

    fun save() = DataStoreObjectControllerCache
        .findNotNull<Guild>()
        .save(
            this,
            DataStoreStorageType.MONGO
        )
}
