package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.diagnostic.DiagnosticCollector
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore
import spike.Include
import spike.compiler.processor.symbol.UnresolvedCompiled

/**
 * Rebuilds the bindings of a compiled production entry point from the declarations its index lists.
 */
@Include
class ProductionGraph(
    private val declarations: IndexedDeclarations,
    private val include: IncludeVisitor,
    private val compiled: UnresolvedCompiled,
) {
    fun of(resolver: Resolver, base: KSClassDeclaration, consumer: KSClassDeclaration): GraphContext {
        val production = GraphContext(GraphStore.Builder(), MultiBindingStore.Builder(), resolver)
        val diagnostics = DiagnosticCollector()
        val indexed = declarations.of(resolver, base)
        compiled.requireDeclarations(indexed, consumer)
        for (declaration in indexed) {
            diagnostics.attempt { declaration.accept(include, production) }
        }
        diagnostics.throwCollected()
        return production
    }
}
