package spike.compiler.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import spike.compiler.generator.DependencyGraphGenerator

class GraphContributorExport(
    override val generator: DependencyGraphGenerator,
    override val environment: SymbolProcessorEnvironment,
    override val logger: KSPLogger,
    override val origins: Sequence<KSAnnotated>,
) : GraphContributorOriginator()