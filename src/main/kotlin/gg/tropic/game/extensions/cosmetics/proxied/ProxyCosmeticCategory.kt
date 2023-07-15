package gg.tropic.game.extensions.cosmetics.proxied

import gg.tropic.game.extensions.cosmetics.CosmeticCategory
import gg.tropic.game.extensions.cosmetics.CosmeticLike

/**
 * Defines a category proxying a separate bucket
 * of non-cosmetic resources. These resources are translated
 * to [CosmeticLike]s when accessed.
 *
 * @author GrowlyX
 * @since 7/14/2023
 */
interface ProxyCosmeticCategory<T> : CosmeticCategory
{
    fun proxied(): List<T>
    fun convert(t: T): CosmeticLike

    override fun cosmetics() = proxied().map(::convert)
}
