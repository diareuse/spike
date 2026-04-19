package spike.compiler.generator

import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory
import spike.factory.DependencyId

class TypeFactoryIdHolder(
    private val limit: Int = 1000
) {
    private val holders = mutableListOf<MutableList<TypeFactory>>(mutableListOf())
    val indices get()=holders.indices

    fun add(typeFactory: TypeFactory): Int {
        var h = holders.last()
        if (h.size >= limit) {
            holders.add(mutableListOf())
            h = holders.last()
        }
        val segment = holders.indexOf(h)
        val index = h.size
        h.add(typeFactory)
        return DependencyId(segment, index).id
    }

    fun getOrAdd(typeFactory: TypeFactory): Int {
        for ((segment, holder) in holders.withIndex()) {
            val index = holder.indexOf(typeFactory)
            if (index >= 0) {
                return DependencyId(segment, index).id
            }
        }
        return add(typeFactory)
    }

    fun find(type: Type) = holders.flatten().first { it.type == type }

    operator fun iterator(): Iterator<List<TypeFactory>> = holders.iterator()
    fun toList(): List<List<TypeFactory>> = holders
}
