package gg.scala.game.extensions.cosmetics.proxied

import gg.scala.game.extensions.cosmetics.CosmeticCategory
import gg.scala.game.extensions.cosmetics.CosmeticLike

interface ProxyCosmeticCategory<T> : CosmeticCategory
{
    fun proxied(): List<T>
    fun convert(t: T): CosmeticLike

    override fun cosmetics() = proxied().map(::convert)
}
