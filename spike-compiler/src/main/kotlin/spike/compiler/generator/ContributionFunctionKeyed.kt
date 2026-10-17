package spike.compiler.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import spike.compiler.generator.code.keyLiteral
import spike.compiler.graph.ExportedContribution
import spike.BindTarget
import spike.Include
import spike.compiler.Order

class ContributionFunctionKeyed(private val delegate: ContributionFunction) : ContributionFunction {
    override fun of(contribution: ExportedContribution, context: FileGeneratorContext): FunSpec {
        val function = delegate.of(contribution, context)
        val key = contribution.key
        val annotation = key?.annotation ?: return function
        val spec = AnnotationSpec.builder(context.resolver.getTypeName(annotation) as ClassName)
            .addMember(context.keyLiteral(key.value))
            .build()
        return function.toBuilder().addAnnotation(spec).build()
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        fun decorator(): ContributionFunctionDecorator = ContributionFunctionDecorator(::ContributionFunctionKeyed)
    }
}
