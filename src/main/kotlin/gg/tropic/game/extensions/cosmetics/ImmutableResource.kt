package gg.tropic.game.extensions.cosmetics

import com.google.gson.*
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import net.evilblock.cubed.serializers.Serializers
import java.lang.reflect.Type

/**
 * Defines a policy or resource for a particular, or wildcard (*) resource
 * under a specified domain (called namespaces).
 *
 * @author GrowlyX
 * @since 7/14/2023
 */
data class ImmutableResource(
    val namespace: String,
    val resource: String?
)
{
    constructor(namespace: String) : this(namespace, null)

    fun withinSameDomainOf(other: ImmutableResource) = other.namespace == namespace
    fun grantsAccessTo(resource: ImmutableResource) =
        resource.namespace == namespace && (this.resource == null || resource.resource == this.resource)

    override fun toString() =
        "$namespace:${if (resource != null) "$resource" else "*"}"
}

@Service
object ImmutableResourceSerializer : JsonSerializer<ImmutableResource>, JsonDeserializer<ImmutableResource>
{
    @Configure
    fun configure()
    {
        Serializers.create {
            registerTypeAdapter(ImmutableResource::class.java, this)
        }
    }

    override fun serialize(
        resource: ImmutableResource,
        p1: Type,
        p2: JsonSerializationContext
    ) = JsonPrimitive(resource.toString())

    override fun deserialize(
        element: JsonElement,
        p1: Type?,
        p2: JsonDeserializationContext?
    ) = element.asString.split(":")
        .let {
            ImmutableResource(
                it[0],
                if (it[1] == "*") null else it[1]
            )
        }
}
