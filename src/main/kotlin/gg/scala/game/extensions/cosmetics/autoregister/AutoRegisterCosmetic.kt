package gg.scala.game.extensions.cosmetics.autoregister

import gg.scala.game.extensions.cosmetics.CosmeticCategory
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class AutoRegisterCosmetic(
    val value: KClass<out CosmeticCategory>
)
