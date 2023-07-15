package gg.tropic.game.extensions.cosmetics

import gg.scala.commons.annotations.custom.CustomAnnotationProcessors
import gg.scala.commons.preconfigure.PreConfigureSubTypeProcessor
import gg.scala.flavor.service.Configure
import gg.tropic.game.extensions.cosmetics.autoregister.AutoRegisterCategory
import gg.tropic.game.extensions.cosmetics.autoregister.AutoRegisterCosmetic

/**
 * Holds default mappings of cosmetic categories to
 * their respective list of cosmetics.
 *
 * @author GrowlyX
 * @since 7/14/2023
 */
object CosmeticRegistry
{
    private val categories = mutableMapOf<String, CosmeticCategory>()
    private val cosmetics = mutableMapOf<CosmeticCategory, List<CosmeticLike>>()

    fun categories() = categories.values.toList()

    fun allEquipOnLoginCosmetics() = cosmetics
        .flatMap { it.value }
        .filterIsInstance<EquipOnLoginCosmetic>()

    fun findRelatedTo(category: CosmeticCategory) =
        cosmetics[category] ?: listOf()

    fun registerCategory(category: CosmeticCategory)
    {
        categories[category.resource.namespace] = category
    }

    @Configure
    fun configure()
    {
        CustomAnnotationProcessors
            .process<AutoRegisterCosmetic> {
                register(
                    it.javaClass
                        .getAnnotation(AutoRegisterCosmetic::class.java)
                        .value.objectInstance!!,
                    it as CosmeticLike
                )
            }

        CustomAnnotationProcessors
            .process<AutoRegisterCategory> {
                registerCategory(it as CosmeticCategory)
            }
    }

    fun register(
        category: CosmeticCategory,
        cosmeticLike: CosmeticLike
    )
    {
        assert(
            category.resource.withinSameDomainOf(cosmeticLike.resource)
        )

        cosmetics.putIfAbsent(category, listOf())
        cosmetics[category] = cosmetics[category]!!
            .toMutableList()
            .apply {
                add(cosmeticLike)
            }
    }
}
