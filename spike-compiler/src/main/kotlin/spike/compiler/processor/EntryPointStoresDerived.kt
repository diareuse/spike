package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.EntryPoint
import spike.compiler.diagnostic.DiagnosticCollector
import spike.compiler.graph.Alias
import spike.compiler.graph.GraphStore
import spike.compiler.processor.symbol.SymbolRegistry
import spike.Include

/**
 * Gives a test entry point that extends a compiled production entry point the production graph, with every
 * @Replaces declaration taking the place of the binding it replaces.
 */
@OptIn(KspExperimental::class)
@Include
class EntryPointStoresDerived(
    private val delegate: EntryPointStoresLocal,
    private val production: ProductionGraph,
    private val registry: SymbolRegistry,
    private val replacements: ReplacementReader,
    private val check: ReplacementCheck,
) : EntryPointStores {

    override fun of(context: GraphContext, entryPoint: KSClassDeclaration): EntryPointStores.Stores {
        val local = delegate.of(context, entryPoint)
        val base = entryPoint.compiledBase() ?: return local
        val graph = production.of(context.resolver, base)
        var root = graph.builder.build()
        val aliases = mutableListOf<Alias>()
        val diagnostics = DiagnosticCollector()
        for (symbol in registry.replaces(context.resolver)) diagnostics.attempt {
            val replacement = replacements.of(symbol, base)
            check.check(replacement, root)
            root = root.without(replacement.replaced)
            replacement.alias?.let(aliases::add)
        }
        diagnostics.throwCollected()
        return EntryPointStores.Stores(
            root = root + local.root + GraphStore(aliases = aliases),
            multibinding = graph.multibind.build() + local.multibinding,
        )
    }

    private fun KSClassDeclaration.compiledBase(): KSClassDeclaration? = superTypes
        .mapNotNull { it.resolve().declaration as? KSClassDeclaration }
        .firstOrNull { it.containingFile == null && it.isAnnotationPresent(EntryPoint::class) }
}
