package spike.compiler.processor

import com.google.devtools.ksp.processing.KSPLogger
import spike.Include

/**
 * Wraps a contributor so that its duration is logged under its name without the `GraphContributor` prefix.
 */
@Include
class GraphContributorTiming(private val logger: KSPLogger) {
    fun timed(contributor: GraphContributor): GraphContributor {
        val tag = contributor::class.simpleName.orEmpty().removePrefix("GraphContributor")
        return GraphContributorMeasureTime(contributor, logger, tag)
    }
}
