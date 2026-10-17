package spike.compiler.generator

import com.squareup.kotlinpoet.FunSpec
import spike.compiler.graph.ExportedContribution
import spike.Include

/**
 * Builds the function through which an @Export module publishes one multibinding contribution to the app.
 */
fun interface ContributionFunction {
    fun of(contribution: ExportedContribution, context: FileGeneratorContext): FunSpec

    companion object {
        @Include
        fun default(
            core: ContributionFunctionCall,
            decorators: Map<Int, ContributionFunctionDecorator>,
        ): ContributionFunction = decorators.toSortedMap().values.fold(core as ContributionFunction) { inner, d ->
            d.decorate(inner)
        }
    }
}
