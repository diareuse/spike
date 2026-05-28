package spike.compiler.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.processor.symbol.SymbolRegistry

class GraphContributorExport(
    override val generator: DependencyGraphGenerator,
    override val environment: SymbolProcessorEnvironment,
    override val logger: KSPLogger,
    private val registry: SymbolRegistry
) : GraphContributorOriginator() {
    override fun getOrigins(resolver: Resolver) = registry.export(resolver)
}
