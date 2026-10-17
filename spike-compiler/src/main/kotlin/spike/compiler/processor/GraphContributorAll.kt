package spike.compiler.processor

import spike.Include

/**
 * Runs every contributor in ascending `@Order`, each timed.
 */
@Include
class GraphContributorAll(
    contributors: Map<Int, GraphContributor>,
    timing: GraphContributorTiming,
) : GraphContributor {
    private val contributors = contributors.toSortedMap().values.map(timing::timed)

    override fun contribute(context: GraphContext) {
        for (contributor in contributors) contributor.contribute(context)
    }
}
