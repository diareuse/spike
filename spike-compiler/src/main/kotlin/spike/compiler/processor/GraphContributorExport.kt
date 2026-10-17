package spike.compiler.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.processor.symbol.SymbolRegistry
import spike.BindTarget
import spike.Include
import spike.compiler.Order
import spike.compiler.generator.ExportGraph

@Include
class GraphContributorExport(
    @ExportGraph override val generator: DependencyGraphGenerator,
    override val environment: SymbolProcessorEnvironment,
    override val logger: KSPLogger,
    override val reporter: DiagnosticReporter,
    override val warnings: WarningReporter,
    override val contributions: ContributionImports,
    private val registry: SymbolRegistry,
    override val stores: EntryPointStoresLocal,
) : GraphContributorOriginator() {
    override fun getOrigins(resolver: Resolver) = registry.export(resolver)

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(30)
        val GraphContributorExport.asGraphContributor: GraphContributor
            get() = this
    }
}
