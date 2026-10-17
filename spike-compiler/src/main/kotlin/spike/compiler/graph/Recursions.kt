package spike.compiler.graph

class Recursions {
    private val pending = HashMap<Type, MutableList<TypeFactory.Recursion>>()

    fun reference(type: Type): TypeFactory.Recursion =
        TypeFactory.Recursion(type).also { pending.getOrPut(type) { mutableListOf() } += it }

    fun resolved(type: Type, factory: TypeFactory) {
        pending.remove(type)?.forEach { it.target = factory }
    }
}
