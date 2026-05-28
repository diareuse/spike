package spike.compiler.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import kotlin.time.measureTime

class GraphContributorMeasureTime(
    private val origin: GraphContributor,
    private val logger: KSPLogger,
    private val tag: String
): GraphContributor {
    override fun contribute(context: GraphContext) {
        val time = measureTime {
            origin.contribute(context)
        }
        logger.info("[$tag] $time")
    }
}
