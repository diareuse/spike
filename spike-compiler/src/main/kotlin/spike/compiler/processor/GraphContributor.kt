package spike.compiler.processor

import com.google.devtools.ksp.processing.Resolver

typealias GraphContributorBuilder = MutableList<GraphContributor>

fun interface GraphContributor {
    fun contribute(context: GraphContext, resolver: Resolver)

    companion object {
        inline fun create(body: GraphContributorBuilder.() -> Unit): GraphContributor {
            return create(mutableListOf<GraphContributor>().apply(body))
        }

        fun create(contributors: List<GraphContributor>): GraphContributor =
            GraphContributorMeta(contributors.toList())
    }
}

private class GraphContributorMeta(
    private val contributors: List<GraphContributor>
) : GraphContributor {
    override fun contribute(
        context: GraphContext,
        resolver: Resolver
    ) {
        for (contributor in contributors)
            contributor.contribute(context, resolver)
    }
}