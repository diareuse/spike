package spike.compiler.processor

import spike.Include

fun interface GraphContributor {
    fun contribute(context: GraphContext)

    companion object {
        @Include
        val GraphContributorAll.contributor: GraphContributor
            get() = this
    }
}
