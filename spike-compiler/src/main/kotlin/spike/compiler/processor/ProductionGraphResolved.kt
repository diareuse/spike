package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.BindTarget
import spike.Include
import spike.compiler.Order
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticUnresolvedCompiled
import spike.compiler.diagnostic.Involved
import spike.compiler.processor.symbol.CompiledSignatureTypes
import spike.compiler.processor.symbol.UnresolvedTypes

class ProductionGraphResolved(
    private val delegate: ProductionGraph,
    private val declarations: IndexedDeclarations,
    private val signatures: CompiledSignatureTypes,
    private val unresolved: UnresolvedTypes,
) : ProductionGraph {
    override fun of(resolver: Resolver, base: KSClassDeclaration, consumer: KSClassDeclaration): GraphContext {
        val types = declarations.of(resolver, base).asSequence().flatMap(signatures::of)
        val consumers = listOf(Involved(consumer.described, consumer.asOrigin()))
        val diagnostics = unresolved.of(types)
            .map { it.type to it.user.description }
            .distinct()
            .map { (type, compiled) -> DiagnosticUnresolvedCompiled(type, compiled, consumers) }
            .toList()
        if (diagnostics.isNotEmpty()) throw DiagnosticException(diagnostics)
        return delegate.of(resolver, base, consumer)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        fun decorator(
            declarations: IndexedDeclarations,
            signatures: CompiledSignatureTypes,
            unresolved: UnresolvedTypes,
        ) = ProductionGraphDecorator { ProductionGraphResolved(it, declarations, signatures, unresolved) }
    }
}
