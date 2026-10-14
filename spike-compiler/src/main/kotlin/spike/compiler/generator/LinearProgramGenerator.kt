package spike.compiler.generator

import com.squareup.kotlinpoet.ANY
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
import spike.Include

@Include
class LinearProgramGenerator(
    private val expression: TypeFactoryExpression,
) {

    private val layouts = LinearLayoutByCost(ChunksByCost(ChunkBudget), ::cost)

    fun generate(
        context: FileGeneratorContext,
        program: LinearProgram,
        index: Int,
        collector: FileSpecCollector,
    ): ClassName {
        val className = context.resolver.peerClass(context.graph, "Program$index")
        val layout = layouts.of(program)
        val type = TypeSpec.objectBuilder(className)
            .addModifiers(KModifier.INTERNAL)
            .addOriginatingFiles(context.originatingFiles)
            .addFunction(createResolve(context, layout))
        for ((part, steps) in layout.parts.withIndex()) {
            type.addFunction(createPart(context, layout, part, steps))
        }
        val file = FileSpec.builder(className)
            .addType(type.build())
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                    .addMember(
                        "%S, %S, %S, %S, %S",
                        "UNCHECKED_CAST",
                        "unused",
                        "RedundantVisibilityModifier",
                        "ClassName",
                        "USELESS_CAST"
                    )
                    .build()
            )
            .build()
        collector.emit(file)
        return className
    }

    private fun createResolve(context: FileGeneratorContext, layout: LinearLayout): FunSpec {
        val body = CodeBlock.builder()
        val arguments = if (layout.slots.isEmpty()) "factory" else "factory, instances"
        if (layout.slots.isNotEmpty()) {
            body.addStatement("val instances = %M<%T>(%L)", ArrayOfNulls, ANY, layout.slots.size)
        }
        for (part in 0 until layout.parts.lastIndex) {
            body.addStatement("part$part($arguments)")
        }
        body.addStatement("return part${layout.parts.lastIndex}($arguments)")
        return FunSpec.builder("resolve")
            .addParameter("factory", context.dependencyFactoryClassName)
            .returns(NullableAny)
            .addCode(body.build())
            .build()
    }

    private fun createPart(
        context: FileGeneratorContext,
        layout: LinearLayout,
        part: Int,
        steps: List<IndexedValue<LinearStep>>,
    ): FunSpec {
        val references = LinearReferences(part, layout)
        val body = CodeBlock.builder()
        for ((index, step) in steps) {
            val local = layout.isLocal(index)
            val slot = layout.slotOf(index)
            when {
                index == layout.root -> body.add("return ")
                local -> body.add("val v%L: %T = ", index, context.resolver.getTypeName(step.factory.type))
                else -> body.add("instances[%L] = ", slot)
            }
            context(context, step, references) {
                expression.write(body)
            }
            if (local && slot != null) {
                body.addStatement("instances[%L] = v%L", slot, index)
            }
        }
        val function = FunSpec.builder("part$part")
            .addModifiers(KModifier.PRIVATE)
            .addParameter("factory", context.dependencyFactoryClassName)
        if (layout.slots.isNotEmpty()) function.addParameter("instances", ArrayOfAny)
        if (part == layout.parts.lastIndex) function.returns(NullableAny)
        return function.addCode(body.build()).build()
    }

    private fun cost(step: LinearStep) = StepCost + ArgumentCost * step.arguments.size

    private companion object {
        const val StepCost = 32
        const val ArgumentCost = 24
        const val ChunkBudget = 4000
        val NullableAny = ANY.copy(nullable = true)
        val ArrayOfNulls = MemberName("kotlin", "arrayOfNulls")
        val ArrayOfAny = Array::class.asClassName().parameterizedBy(NullableAny)
    }
}
