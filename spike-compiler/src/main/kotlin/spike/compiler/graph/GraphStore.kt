package spike.compiler.graph

data class GraphStore(
    val constructors: List<Constructor> = emptyList(),
    val factories: List<Factory> = emptyList(),
) {

    operator fun plus(other: GraphStore) = GraphStore(
        constructors = constructors + other.constructors,
        factories = factories + other.factories,
    )

    private val constructorsByType by lazy(LazyThreadSafetyMode.NONE) { constructors.groupBy { it.type } }
    private val factoriesByType by lazy(LazyThreadSafetyMode.NONE) { factories.groupBy { it.type } }

    fun constructorOf(type: Type): Constructor? = constructorsByType[type]?.first()

    fun factoryOf(type: Type): Factory? = factoriesByType[type]?.first()

    fun bindingsOf(type: Type): List<Binding> = buildList {
        constructorsByType[type]?.let(::addAll)
        factoriesByType[type]?.let(::addAll)
    }.distinct()

    fun overriddenBy(definition: GraphStore): GraphStore {
        val types = (definition.constructors + definition.factories).map { it.type }.toSet()
        return definition + GraphStore(
            constructors = constructors.filterNot { it.type in types },
            factories = factories.filterNot { it.type in types },
        )
    }

    class Builder {

        private val constructors = mutableSetOf<Constructor>()
        private val factories = mutableSetOf<Factory>()

        fun addConstructor(type: Type, invocation: Invocation, singleton: Boolean) {
            constructors += Constructor(type, invocation, singleton)
        }

        fun addFactory(type: Type, member: Member, invocation: Invocation, singleton: Boolean) {
            factories += Factory(type, member, invocation, singleton)
        }

        fun build() = GraphStore(
            constructors = constructors.toList(),
            factories = factories.toList(),
        )
    }
}
