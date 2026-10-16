package spike.compiler.generator

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asClassName
import spike.compiler.graph.ExportedContribution
import spike.factory.DependencyId

class ContributionFunctionCall : ContributionFunction {
    override fun of(contribution: ExportedContribution, context: FileGeneratorContext): FunSpec =
        FunSpec.builder(contribution.name)
            .returns(context.resolver.getTypeName(contribution.factory.type))
            .addStatement(
                "return factory.get(%L(%L))",
                DependencyId::class.asClassName(),
                context.getDependencyId(contribution.factory),
            )
            .build()
}
