package spike.compiler.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import spike.Include

@Include
class LinearProgramGenerator(
    private val expression: TypeFactoryExpression,
) {

    private val chunks = ChunksByCost(ChunkBudget)

    fun generate(
        context: FileGeneratorContext,
        program: LinearProgram,
        index: Int,
        collector: FileSpecCollector,
    ): ClassName {
        val className = context.resolver.peerClass(context.graph, "Program$index")
        val type = TypeSpec.objectBuilder(className)
            .addModifiers(KModifier.INTERNAL)
            .addOriginatingFiles(context.originatingFiles)
        val parts = chunks.split(program.steps.withIndex().toList()) { (_, step) -> cost(step) }
        type.addFunction(createResolve(context, program, parts.size))
        for ((part, steps) in parts.withIndex()) {
            type.addFunction(createPart(context, part, steps))
        }
        val file = FileSpec.builder(className)
            .addType(type.build())
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                    .addMember(
                        "%S, %S, %S, %S",
                        "UNCHECKED_CAST",
                        "unused",
                        "RedundantVisibilityModifier",
                        "ClassName"
                    )
                    .build()
            )
            .build()
        collector.emit(file)
        return className
    }

    private fun createResolve(context: FileGeneratorContext, program: LinearProgram, parts: Int): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val instances = %M<%T>(%L)", ArrayOfNulls, Any::class, program.steps.size)
        for (part in 0 until parts) {
            body.addStatement("part$part(factory, instances)")
        }
        body.addStatement("return instances[%L]!!", program.steps.lastIndex)
        return FunSpec.builder("resolve")
            .addParameter("factory", context.dependencyFactoryClassName)
            .returns(Any::class)
            .addCode(body.build())
            .build()
    }

    private fun createPart(
        context: FileGeneratorContext,
        part: Int,
        steps: List<IndexedValue<LinearStep>>,
    ): FunSpec {
        val body = CodeBlock.builder()
        for ((index, step) in steps) {
            body.add("instances[%L] = ", index)
            context(context, step) {
                expression.write(body)
            }
        }
        return FunSpec.builder("part$part")
            .addModifiers(KModifier.PRIVATE)
            .addParameter("factory", context.dependencyFactoryClassName)
            .addParameter("instances", ArrayOfAny)
            .addCode(body.build())
            .build()
    }

    private fun cost(step: LinearStep) = StepCost + ArgumentCost * step.arguments.size

    private companion object {
        const val StepCost = 32
        const val ArgumentCost = 24
        const val ChunkBudget = 4000
        val ArrayOfNulls = MemberName("kotlin", "arrayOfNulls")
        val ArrayOfAny = Array::class.asClassName()
            .parameterizedBy(Any::class.asTypeName().copy(nullable = true))
    }
}
