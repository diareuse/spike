package spike.compiler.generator

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import spike.Include
import spike.compiler.graph.TypeFactory

@Include
class LinearProgramGenerator(
    private val expression: TypeFactoryExpression,
) {

    private val layouts = LinearLayoutByCost(ChunksByCost(ChunkBudget), ::cost)
    private val values = listOf(ProgramValueInstances(), ProgramValueNeeded())

    fun generate(
        context: FileGeneratorContext,
        program: LinearProgram,
        index: Int,
        collector: FileSpecCollector,
    ): ClassName {
        val className = context.resolver.peerClass(context.graph, "Program$index")
        val layout = layouts.of(program)
        val guards = guardsOf(context).of(program)
        val programValues = values.mapNotNull { it.of(program, layout, guards) }
        val type = TypeSpec.objectBuilder(className)
            .addModifiers(KModifier.INTERNAL)
            .addOriginatingFiles(context.originatingFiles)
        type.addFunction(createResolve(context, layout, programValues, type))
        for ((part, steps) in layout.parts.withIndex()) {
            type.addFunction(createPart(context, layout, guards, programValues, part, steps))
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

    private fun guardsOf(context: FileGeneratorContext) = LinearGuardsOf(GuardTableLimit) { step ->
        (step.factory as? TypeFactory.Callable)?.takeIf { it.singleton }?.let(context::getDependencyId)
    }

    private fun createResolve(
        context: FileGeneratorContext,
        layout: LinearLayout,
        values: List<ProgramValue>,
        program: TypeSpec.Builder,
    ): FunSpec {
        val body = CodeBlock.builder()
        val resolve = FunSpec.builder("resolve").addParameter("factory", context.dependencyFactoryClassName)
        for (value in values) value.declare(program, resolve, body)
        val arguments = (listOf("factory") + values.map { it.parameter.name }).joinToString()
        for (part in 0 until layout.parts.lastIndex) {
            body.addStatement("part$part($arguments)")
        }
        body.addStatement("return part${layout.parts.lastIndex}($arguments)")
        return resolve.returns(NullableAny).addCode(body.build()).build()
    }

    private fun createPart(
        context: FileGeneratorContext,
        layout: LinearLayout,
        guards: LinearGuards,
        values: List<ProgramValue>,
        part: Int,
        steps: List<IndexedValue<LinearStep>>,
    ): FunSpec {
        val references = LinearReferences(part, layout, guards)
        val body = CodeBlock.builder()
        for ((index, step) in steps) {
            val local = layout.isLocal(index)
            val slot = layout.slotOf(index)
            val guard = guards.guardOf(index)
            val declared = context.resolver.getTypeName(step.factory.type)
            val type = declared.copy(nullable = declared.isNullable || guard != null)
            when {
                index == layout.root -> body.add("return ")
                local -> body.add("val v%L: %T = ", index, type)
                else -> body.add("instances[%L] = ", slot)
            }
            if (guard != null) body.add("if (!needed[%L]) null else ", guard)
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
            .addParameters(values.map { it.parameter })
        if (part == layout.parts.lastIndex) function.returns(NullableAny)
        return function.addCode(body.build()).build()
    }

    private fun cost(step: LinearStep) = StepCost + ArgumentCost * step.arguments.size

    private companion object {
        const val StepCost = 32
        const val ArgumentCost = 24
        const val ChunkBudget = 4000
        const val GuardTableLimit = 16_000
        val NullableAny = ANY.copy(nullable = true)
    }
}
