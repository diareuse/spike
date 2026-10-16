package spike.compiler.generator

import com.squareup.kotlinpoet.FunSpec
import spike.compiler.graph.ExportedContribution

/**
 * Builds the function through which an @Export module publishes one multibinding contribution to the app.
 */
fun interface ContributionFunction {
    fun of(contribution: ExportedContribution, context: FileGeneratorContext): FunSpec
}
