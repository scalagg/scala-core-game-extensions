package gg.tropic.game.extensions.cosmetics

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import net.evilblock.cubed.serializers.Serializers
import net.evilblock.cubed.serializers.impl.AbstractTypeSerializer

/**
 * @author GrowlyX
 * @since 7/15/2023
 */
@Service
object DynamicCosmeticService
{
    @Configure
    fun configure()
    {
        Serializers.create {
            registerTypeAdapter(
                DynamicCosmeticConfigStore::class.java,
                AbstractTypeSerializer<DynamicCosmeticConfigStore>()
            )
        }
    }
}
