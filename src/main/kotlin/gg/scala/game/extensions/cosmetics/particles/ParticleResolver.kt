package gg.scala.game.extensions.cosmetics.particles

import net.evilblock.cubed.util.ServerVersion
import net.evilblock.cubed.util.nms.MinecraftReflection

object ParticleResolver
{
    fun resolve(legacyName: String, modernName: String): Any
    {
        return if (ServerVersion.getVersion().isNewerThan(ServerVersion.v1_20_R3)) {
            val particleClass = Class.forName("org.bukkit.Particle") as Class<out Enum<*>>
            java.lang.Enum.valueOf(particleClass, modernName)
        } else {
            MinecraftReflection.getEnumParticle(legacyName)!!
        }
    }
}
