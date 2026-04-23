package gg.scala.game.extensions.customtags

import gg.scala.commons.annotations.Model
import gg.scala.store.controller.annotations.Indexed
import gg.scala.store.storage.storable.IDataStoreObject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import java.util.*

@Model
data class CustomChatTag(
    override val identifier: UUID = UUID.randomUUID(),
    @Indexed
    val ownerId: UUID,
    val design: TextComponent,
    var verification: gg.scala.game.extensions.customtags.Verification? = null
) : IDataStoreObject
