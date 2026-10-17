package spike.compiler.graph

import spike.BindTarget
import spike.compiler.diagnostic.Involved

class ImportedContributions {

    private val byName = LinkedHashMap<String, Entry>()

    fun put(target: BindTarget, key: Key?, external: TypeFactory.External, involved: Involved) {
        byName.getOrPut(external.name) { Entry(target, key, external, involved) }
    }

    fun collection(target: BindTarget, type: Type): List<TypeFactory.External> =
        byName.values.filter { it.target == target && it.external.type == type }.map { it.external }

    fun map(value: Type): Map<Key, List<Entry>> =
        byName.values.filter { it.target == BindTarget.Map && it.external.type == value }.groupBy { it.key!! }

    class Entry(
        val target: BindTarget,
        val key: Key?,
        val external: TypeFactory.External,
        val involved: Involved,
    )
}
