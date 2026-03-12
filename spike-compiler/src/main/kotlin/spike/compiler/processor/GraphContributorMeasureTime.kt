package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import kotlin.time.measureTime

class GraphContributorMeasureTime(
    private val origin: GraphContributor,
    private val environment: SymbolProcessorEnvironment,
    private val tag: String
): GraphContributor {
    override fun contribute(context: GraphContext, resolver: Resolver) {
        val time = measureTime {
            origin.contribute(context, resolver)
        }
        environment.logger.info("[$tag] $time")
    }
}
