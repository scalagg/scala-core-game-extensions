package gg.scala.game.extensions.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import net.evilblock.cubed.serializers.Serializers
import net.evilblock.cubed.serializers.impl.AbstractTypeSerializer

@Service
object DynamicCosmeticService
{
    @Configure
    fun configure()
    {
        Serializers.create {
            registerTypeAdapter(
                gg.scala.game.extensions.cosmetics.DynamicCosmeticConfigStore::class.java,
                AbstractTypeSerializer<gg.scala.game.extensions.cosmetics.DynamicCosmeticConfigStore>()
            )
        }
    }
}
