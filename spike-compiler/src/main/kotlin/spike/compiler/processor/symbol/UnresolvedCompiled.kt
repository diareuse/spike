package spike.compiler.processor.symbol

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import spike.Include
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticUnresolvedType
import spike.compiler.diagnostic.Involved
import spike.compiler.processor.asOrigin
import spike.compiler.processor.described

@Include
class UnresolvedCompiled(
    private val unresolved: UnresolvedTypes,
    private val declarations: SignatureTypes,
    private val exports: SignatureTypesExport,
) {
    fun requireExports(generated: List<KSClassDeclaration>, consumer: KSDeclaration) =
        require(generated.asSequence().flatMap(exports::of), consumer)

    fun requireDeclarations(compiled: List<KSAnnotated>, consumer: KSDeclaration) =
        require(compiled.asSequence().flatMap(declarations::of), consumer)

    private fun require(types: Sequence<SignatureType>, consumer: KSDeclaration) {
        val user = Involved(consumer.described, consumer.asOrigin())
        val diagnostics = unresolved.of(types)
            .distinctBy { it.type to it.user.description }
            .map { DiagnosticUnresolvedType(it.type, listOf(it.user, user)) }
            .toList()
        if (diagnostics.isNotEmpty()) throw DiagnosticException(diagnostics)
    }
}
