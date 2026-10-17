package spike.compiler.graph

import spike.BindTarget

class ExportedContribution(
    val target: BindTarget,
    val key: Key?,
    val factory: TypeFactory,
    val name: String,
    val type: Type = factory.type,
) {
    companion object {
        fun nameOf(target: BindTarget, binding: Binding): String =
            prefixOf(target) + "${binding.description}: ${binding.type.displayName}".escapedName()

        fun targetOf(name: String): BindTarget? =
            BindTarget.entries.firstOrNull { it != BindTarget.None && name.startsWith(prefixOf(it)) }

        fun describe(name: String, target: BindTarget): String = name.removePrefix(prefixOf(target)).unescapedName()

        private fun prefixOf(target: BindTarget) = "spike_${target.name.lowercase()}_"
    }
}
