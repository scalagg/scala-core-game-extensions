package gg.scala.game.extensions.leveling

import gg.scala.commons.agnostic.sync.ServerSync
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.game.extensions.profile.CorePlayerProfileService
import gg.scala.lemon.channel.channels.DefaultChatChannel
import net.evilblock.cubed.util.CC
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

/**
 * @author Limo
 * @date 2/14/2026
 */
@Service
object LevelChatOverrideService
{
    @Configure
    fun configure()
    {
        val serverGroups = ServerSync.getLocalGameServer().groups

        val matchingSystem = LevelSystemSyncService.all().values
            .firstOrNull { system ->
                system.primaryGroupChatOverrides.any { override ->
                    override in serverGroups
                }
            }
            ?: return

        val systemId = matchingSystem.id

        DefaultChatChannel.provideAdditionalPrefix { player ->
            val profile = CorePlayerProfileService.find(player)
            val formattedLevel = profile?.getFormattedGameLevel(systemId)
                ?: "${CC.GRAY}[1\u272B${CC.GRAY}]"

            LegacyComponentSerializer.legacySection()
                .deserialize("$formattedLevel ")
        }
    }
}
