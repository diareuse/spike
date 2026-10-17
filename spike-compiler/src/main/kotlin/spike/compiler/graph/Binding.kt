package spike.compiler.graph

import spike.compiler.diagnostic.DiagnosticDuplicateBinding
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.Involved
import spike.compiler.diagnostic.Origin

sealed interface Binding {
    val type: Type
    val description: String
    val origin: Origin?
    fun asGraphStore(): GraphStore
    fun retyped(type: Type): Binding
}

internal val Type.displayName: String
    get() = toString().replace(RootPackagePrefix, "")

private val RootPackagePrefix = Regex("""(?<![\w.])\.""")

internal val Binding.involved: Involved
    get() = Involved(description, origin)

internal fun failOnDuplicateBindings(subject: String, bindings: List<Binding>): Nothing =
    throw DiagnosticException(DiagnosticDuplicateBinding(subject, bindings.map { it.involved }))
