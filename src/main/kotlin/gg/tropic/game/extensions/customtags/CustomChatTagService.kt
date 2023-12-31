package gg.tropic.game.extensions.customtags

import com.mongodb.client.model.Filters
import gg.scala.flavor.service.Service
import gg.scala.store.controller.DataStoreObjectControllerCache
import java.util.*

@Service
object CustomChatTagService
{
    fun getChatTagsFromPlayer(target: UUID) = DataStoreObjectControllerCache
        .findNotNull<CustomChatTag>()
        .mongo()
        .loadAllWithFilter(
            Filters.eq("ownerId", target.toString())
        )
        .thenApply { it.values.toList() }

    fun save(tag: CustomChatTag) = DataStoreObjectControllerCache
        .findNotNull<CustomChatTag>()
        .save(tag)
}