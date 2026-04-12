package gg.scala.game.extensions.guilds

import gg.scala.lemon.util.QuickAccess
import java.util.Date
import java.util.UUID

data class GuildMember(
    val uniqueId: UUID,
    val joinedOn: Date = Date(),
    var role: GuildRole = GuildRole.Member
)
{
    fun online() = QuickAccess.online(uniqueId)
}
