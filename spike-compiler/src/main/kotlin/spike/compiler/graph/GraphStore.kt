package spike.compiler.graph

data class GraphStore(
    val constructors: List<Constructor> = emptyList(),
    val factories: List<Factory> = emptyList(),
    val binders: List<Binder> = emptyList(),
) {

    operator fun plus(other: GraphStore) = GraphStore(
        constructors = constructors + other.constructors,
        factories = factories + other.factories,
        binders = binders + other.binders,
    )

    private val constructorsByType by lazy(LazyThreadSafetyMode.NONE) { constructors.groupBy { it.type } }
    private val factoriesByType by lazy(LazyThreadSafetyMode.NONE) { factories.groupBy { it.type } }
    private val bindersByType by lazy(LazyThreadSafetyMode.NONE) { binders.groupBy { it.type } }

    fun constructorOf(type: Type): Constructor? = constructorsByType[type]?.first()

    fun factoryOf(type: Type): Factory? = factoriesByType[type]?.first()

    fun binderOf(type: Type): Binder? = bindersByType[type]?.first()

    fun bindingsOf(type: Type): List<Binding> = buildList {
        constructorsByType[type]?.let(::addAll)
        factoriesByType[type]?.let(::addAll)
        bindersByType[type]?.let(::addAll)
    }.distinct()

    fun overriddenBy(definition: GraphStore): GraphStore {
        val types = (definition.constructors + definition.factories + definition.binders).map { it.type }.toSet()
        return definition + GraphStore(
            constructors = constructors.filterNot { it.type in types },
            factories = factories.filterNot { it.type in types },
            binders = binders.filterNot { it.type in types },
        )
    }

    class Builder {

        private val constructors = mutableSetOf<Constructor>()
        private val factories = mutableSetOf<Factory>()
        private val binders = mutableSetOf<Binder>()

        fun addConstructor(type: Type, invocation: Invocation, singleton: Boolean) {
            constructors += Constructor(type, invocation, singleton)
        }

        fun addFactory(type: Type, member: Member.Method, invocation: Invocation, singleton: Boolean) {
            factories += Factory(type, member, invocation, singleton)
        }

        fun addBinder(from: Type, to: Type) {
            binders += Binder(from, to)
        }

        fun build() = GraphStore(
            constructors = constructors.toList(),
            factories = factories.toList(),
            binders = binders.toList(),
        )
    }
}
