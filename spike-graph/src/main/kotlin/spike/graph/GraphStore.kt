package spike.graph

data class GraphStore(
    val constructors: List<Constructor>,
    val factories: List<Factory>,
    val binders: List<Binder>
) {

    operator fun plus(other: GraphStore) = GraphStore(
        constructors = constructors + other.constructors,
        factories = factories + other.factories,
        binders = binders + other.binders
    )

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
            binders = binders.toList()
        )
    }
}