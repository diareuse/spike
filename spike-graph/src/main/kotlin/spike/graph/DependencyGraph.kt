package spike.graph

import spike.graph.TypeFactoryHolder.Companion.getOrPut

class DependencyGraph(
    val entry: GraphEntryPoint,
    val methods: List<TypeFactory>,
    val properties: List<TypeFactory>
) {

    operator fun iterator() = iterator {
        val queue = mutableListOf<TypeFactory>()
        val emitted = mutableSetOf<Type>()
        queue.addAll(methods)
        queue.addAll(properties)
        while (queue.isNotEmpty()) {
            val item = queue.removeFirst()
            if (emitted.add(item.type)) {
                yield(item)
                queue.addAll(item.dependencies)
            }
        }
    }

    class Builder(private val logger: Logger) {

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

        fun build(entry: GraphEntryPoint): DependencyGraph {
            val holder = TypeFactoryHolder()
            val factory = entry.factory
            for (p in factory.method.parameters) {
                holder.insert(p.type, TypeFactory.Property(p.type, p.name))
            }
            val methods = entry.methods.map {
                holder.getOrPut(it.returns) { createTypeFactory(it, this, Tail(it)) }
            }
            val properties = entry.properties.map {
                holder.getOrPut(it.returns) { createTypeFactory(it, this, Tail(it)) }
            }
            return DependencyGraph(
                entry = entry,
                methods = methods,
                properties = properties
            )
        }

        // ---

        private fun findConstructor(type: Type) = constructors.find { it.type == type }
        private fun findFactory(type: Type) = factories.find { it.type == type }
        private fun findBinders(type: Type) = binders.find { it.type == type }

        private fun createTypeFactory(type: Type, holder: TypeFactoryHolder, tail: Tail): TypeFactory {
            if (!tail.isStart && tail.startsWith(type)) error("Circular dependency detected: $tail")
            val constructor = findConstructor(type)
            if (constructor != null) {
                return TypeFactory.Class(
                    type = type,
                    invocation = constructor.invocation,
                    singleton = constructor.singleton,
                    dependencies = constructor.invocation.parameters.map {
                        holder.getOrPut(it.type) { createTypeFactory(it, this, tail.then(it)) }
                    },
                    isPublic = tail.isStart
                )
            }
            val factory = findFactory(type)
            if (factory != null) {
                return TypeFactory.Method(
                    type = type,
                    member = factory.member,
                    invocation = factory.invocation,
                    singleton = factory.singleton,
                    dependencies = factory.invocation.parameters.map {
                        holder.getOrPut(it.type) { createTypeFactory(it, this, tail.then(it)) }
                    },
                    isPublic = tail.isStart
                )
            }
            val binder = findBinders(type)
            if (binder != null) {
                return TypeFactory.Binds(
                    type = type,
                    source = holder.getOrPut(binder.source) { createTypeFactory(it, this, tail.then(it)) },
                    isPublic = tail.isStart
                )
            }

            if (type is Type.Parametrized) {
                if (type.envelope == LazyType) {
                    val actualType = type.typeArguments.single()
                    return createTypeFactory(actualType, holder, tail)
                }
                if (type.envelope != ProviderType)
                    error("Parameterized type $type cannot be resolved to type factory. If this is a provider, use spike.Provider<your.type.Here>")
                val actualType = type.typeArguments.single()
                val tf = createTypeFactory(actualType, holder, tail)
                return TypeFactory.Provides(actualType, tf, tail.isStart)
            }

            error("Cannot find factory for $type")
        }

        // ---

        class Tail(
            val type: Type,
            val parent: Tail? = null
        ) {
            val isStart get() = parent == null
            fun then(type: Type) = Tail(type, this)
            fun startsWith(type: Type): Boolean {
                var curr = this
                while (true) {
                    curr = curr.parent ?: break
                }
                return curr.type == type
            }

            override fun toString(): String {
                var curr = parent
                var out = "$type"
                while (curr != null) {
                    out += "\n"
                    out += " -> " + (curr.type)
                    curr = curr.parent
                }
                return out
            }
        }

        class Constructor(
            val type: Type,
            val invocation: Invocation,
            val singleton: Boolean
        )

        class Factory(
            val type: Type,
            val member: Member.Method,
            val invocation: Invocation,
            val singleton: Boolean
        )

        class Binder(
            val source: Type,
            val type: Type
        )

    }

}

val ProviderType = Type.Simple("spike", "Provider")
val LazyType = Type.Simple("kotlin", "Lazy")
val AnyType = Type.Simple(packageName = "kotlin", simpleName = "Any")
