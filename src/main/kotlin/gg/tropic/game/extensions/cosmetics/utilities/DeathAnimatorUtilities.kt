package gg.tropic.game.extensions.cosmetics.utilities

import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy
import com.comphenix.packetwrapper.WrapperPlayServerEntityStatus
import com.comphenix.packetwrapper.WrapperPlayServerEntityVelocity
import net.evilblock.cubed.util.Reflection
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.nms.MinecraftProtocol
import net.evilblock.cubed.util.nms.MinecraftReflection
import net.minecraft.server.v1_8_R3.EntityHuman
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import java.lang.reflect.Field

/**
 * @author GrowlyX
 * @since 12/23/2023
 */
object DeathAnimatorUtilities
{
    private val CURRENT_TICK_FIELD: Field
    private var SPAWN_PACKET_ID_FIELD: Field = PacketPlayOutNamedEntitySpawn::class.java.getDeclaredField("a")

    init
    {
        val minecraftServerClass = MinecraftReflection.getNMSClass("MinecraftServer")!!
        CURRENT_TICK_FIELD = Reflection.getField(minecraftServerClass, "currentTick")!!
        SPAWN_PACKET_ID_FIELD.setAccessible(true)
    }

    fun animate(player: Player)
    {
        val statusPacket = WrapperPlayServerEntityStatus()
        statusPacket.entityID = player.entityId
        statusPacket.entityStatus = 3.toByte()

        player.world.players
            .filter { it != player }
            .forEach {
                statusPacket.sendPacket(it)
            }
        }
    }
}
