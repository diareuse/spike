package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.BindTarget
import spike.Include
import spike.compiler.Order

class ProductionGraphMemo(private val delegate: ProductionGraph) : ProductionGraph {
    private val graphs = mutableMapOf<String?, GraphContext>()

    override fun of(resolver: Resolver, base: KSClassDeclaration, consumer: KSClassDeclaration): GraphContext =
        graphs.getOrPut(base.qualifiedName?.asString()) { delegate.of(resolver, base, consumer) }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(20)
        fun decorator() = ProductionGraphDecorator(::ProductionGraphMemo)
    }
}
