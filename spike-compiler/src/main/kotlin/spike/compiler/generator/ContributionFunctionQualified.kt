package spike.compiler.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import spike.compiler.generator.code.keyLiteral
import spike.compiler.graph.ExportedContribution
import spike.compiler.graph.Type

class ContributionFunctionQualified(private val delegate: ContributionFunction) : ContributionFunction {
    override fun of(contribution: ExportedContribution, context: FileGeneratorContext): FunSpec {
        val qualifiers = (contribution.factory.type as? Type.Qualified)?.qualifiers.orEmpty()
        val annotations = qualifiers.map { qualifier ->
            val annotation = AnnotationSpec.builder(context.resolver.getTypeName(qualifier.type) as ClassName)
            for (argument in qualifier.arguments) {
                annotation.addMember("%L = %L", argument.name, context.keyLiteral(argument.value))
            }
            annotation.build()
        }
        return delegate.of(contribution, context).toBuilder().addAnnotations(annotations).build()
    }
}
