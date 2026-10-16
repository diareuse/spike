package spike.compiler.graph

import spike.compiler.diagnostic.Origin

data class GraphStore(
    val constructors: List<Constructor> = emptyList(),
    val factories: List<Factory> = emptyList(),
    val arguments: List<Argument> = emptyList(),
) {

    operator fun plus(other: GraphStore) = GraphStore(
        constructors = constructors + other.constructors,
        factories = factories + other.factories,
        arguments = arguments + other.arguments,
    )

    private val constructorsByType by lazy(LazyThreadSafetyMode.NONE) { constructors.groupBy { it.type } }
    private val factoriesByType by lazy(LazyThreadSafetyMode.NONE) { factories.groupBy { it.type } }

    fun constructorOf(type: Type): Constructor? = constructorsByType[type]?.first()

    fun factoryOf(type: Type): Factory? = factoriesByType[type]?.first()

    fun argumentOf(type: Type): Argument? = arguments.firstOrNull { it.type == type }

    fun bindingsOf(type: Type): List<Binding> = buildList {
        constructorsByType[type]?.let(::addAll)
        factoriesByType[type]?.let(::addAll)
        addAll(arguments.filter { it.type == type })
    }.distinct()

    fun overriddenBy(definition: GraphStore): GraphStore {
        val types = (definition.constructors + definition.factories).map { it.type }.toSet()
        return definition + GraphStore(
            constructors = constructors.filterNot { it.type in types },
            factories = factories.filterNot { it.type in types },
            arguments = arguments,
        )
    }

    class Builder {

        private val constructors = mutableSetOf<Constructor>()
        private val factories = mutableSetOf<Factory>()

        fun addConstructor(type: Type, invocation: Invocation, singleton: Boolean, origin: Origin? = null) {
            constructors += Constructor(type, invocation, singleton, origin)
        }

        fun addFactory(type: Type, member: Member, invocation: Invocation, singleton: Boolean, origin: Origin? = null) {
            factories += Factory(type, member, invocation, singleton, origin)
        }

        fun build() = GraphStore(
            constructors = constructors.toList(),
            factories = factories.toList(),
        )
    }
}
