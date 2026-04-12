package gg.scala.game.extensions.cosmetics

import gg.scala.commons.annotations.custom.CustomAnnotationProcessors
import gg.scala.flavor.service.Configure
import gg.scala.game.extensions.cosmetics.autoregister.AutoRegisterCategory
import gg.scala.game.extensions.cosmetics.autoregister.AutoRegisterCosmetic
import gg.scala.game.extensions.cosmetics.proxied.ProxyCosmeticCategory
import org.bukkit.entity.Player

object CosmeticRegistry
{
    private val categories = mutableMapOf<String, CosmeticCategory>()
    val cosmetics = mutableMapOf<CosmeticCategory, List<CosmeticLike>>()

    fun categories() = categories.values.toList()

    fun allEquipOnLoginCosmetics() = cosmetics
        .flatMap { it.value }
        .filterIsInstance<EquipOnLogin>()

    fun findRelatedTo(category: CosmeticCategory) = runCatching {
        if (category is ProxyCosmeticCategory<*>)
        {
            category.cosmetics()
        } else
        {
            cosmetics[category] ?: listOf()
        }
    }.getOrElse {
        it.printStackTrace()
        listOf()
    }

    fun getSingleEquipped(category: CosmeticCategory, player: Player) = findRelatedTo(category)
        .firstOrNull {
            it.equipped(player)
        }

    inline fun <reified T : CosmeticLike> getSingleEquippedAs(category: CosmeticCategory, player: Player) = findRelatedTo(category)
        .filterIsInstance<T>()
        .firstOrNull {
            it.equipped(player)
        }

    fun getAllEquipped(category: CosmeticCategory, player: Player) = findRelatedTo(category)
        .filter {
            it.equipped(player)
        }

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
        cosmetics[category] = findRelatedTo(category)
            .toMutableList()
            .apply {
                add(cosmeticLike)
            }
    }
}
