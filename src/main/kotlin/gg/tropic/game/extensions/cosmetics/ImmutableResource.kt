package gg.tropic.game.extensions.cosmetics

/**
 * Defines a policy or resource for a particular, or wildcard (*) resource
 * under a specified domain (called namespaces).
 *
 * @author GrowlyX
 * @since 7/14/2023
 */
data class ImmutableResource(
    val namespace: String,
    val resource: String
)
{
    constructor(namespace: String) : this(namespace, "*")

    fun withinSameDomainOf(other: ImmutableResource) = other.namespace == namespace
    fun grantsAccessTo(resource: ImmutableResource) =
        (namespace == "*" || resource.namespace == namespace) && (this.resource == "*" || resource.resource == this.resource)

    override fun toString() = "$namespace:$resource"

    companion object
    {
        fun of(stringOf: String): ImmutableResource
        {
            val (namespace, resource) = stringOf.split(":")
            return ImmutableResource(namespace, resource)
        }
    }
}
