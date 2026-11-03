package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include

fun interface ProductionGraph {
    fun of(resolver: Resolver, base: KSClassDeclaration, consumer: KSClassDeclaration): GraphContext

    companion object {
        @Include
        fun default(
            core: ProductionGraphIndexed,
            decorators: Map<Int, ProductionGraphDecorator>,
        ): ProductionGraph = decorators.toSortedMap().values.fold(core as ProductionGraph) { inner, decorator ->
            decorator.decorate(inner)
        }
    }
}

fun interface ProductionGraphDecorator {
    fun decorate(delegate: ProductionGraph): ProductionGraph
}
