package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.diagnostic.DiagnosticCollector
import spike.compiler.graph.GraphStore
import spike.compiler.graph.MultiBindingStore
import spike.Include

/**
 * Rebuilds the bindings of a compiled production entry point from the declarations its index lists.
 */
@Include
class ProductionGraph(
    private val declarations: IndexedDeclarations,
    private val include: IncludeVisitor,
) {
    fun of(resolver: Resolver, base: KSClassDeclaration): GraphContext {
        val production = GraphContext(GraphStore.Builder(), MultiBindingStore.Builder(), resolver)
        val diagnostics = DiagnosticCollector()
        for (declaration in declarations.of(resolver, base)) {
            diagnostics.attempt { declaration.accept(include, production) }
        }
        diagnostics.throwCollected()
        return production
    }
}
