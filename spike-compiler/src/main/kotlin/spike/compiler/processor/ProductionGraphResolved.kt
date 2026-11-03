package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.BindTarget
import spike.Include
import spike.compiler.Order
import spike.compiler.diagnostic.Origin
import spike.compiler.graph.Binding
import spike.compiler.processor.symbol.CompiledSignatureTypes
import spike.compiler.processor.symbol.UnresolvedCompiledType
import spike.compiler.processor.symbol.UnresolvedTypes

class ProductionGraphResolved(
    private val delegate: ProductionGraph,
    private val signatures: CompiledSignatureTypes,
    private val unresolved: UnresolvedTypes,
) : ProductionGraph {
    override fun of(resolver: Resolver, base: KSClassDeclaration): CompiledGraph {
        val graph = delegate.of(resolver, base)
        val types = graph.context.bindings()
            .mapNotNull { it.origin as? OriginNode }
            .distinct()
            .mapNotNull { origin -> (origin.node as? KSAnnotated)?.let { origin to unresolvedIn(it) } }
            .filter { (_, types) -> types.isNotEmpty() }
            .toMap<Origin, List<UnresolvedCompiledType>>()
        return CompiledGraph(graph.context, types)
    }

    private fun unresolvedIn(declaration: KSAnnotated) = unresolved.of(signatures.of(declaration))
        .map { UnresolvedCompiledType(it.type, it.user.description) }
        .distinct()
        .toList()

    private fun GraphContext.bindings(): Sequence<Binding> {
        val multibinding = multibind.build()
        val stores = sequenceOf(builder.build()) + multibinding.set.values + multibinding.list.values +
            multibinding.map.values.flatMap { it.values }
        return stores.flatMap { it.constructors + it.factories }
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        fun decorator(
            signatures: CompiledSignatureTypes,
            unresolved: UnresolvedTypes,
        ) = ProductionGraphDecorator { ProductionGraphResolved(it, signatures, unresolved) }
    }
}
