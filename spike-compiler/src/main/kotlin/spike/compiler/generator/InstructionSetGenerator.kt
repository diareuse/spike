package spike.compiler.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import spike.factory.InstructionSet

class InstructionSetGenerator(
    private val sizeLimit: Int = 1000
) : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val instructionSetClassName = context.resolver.peerClass(context.graph, "InstructionSet")
        val dfis = context.instructions
        val type = TypeSpec.objectBuilder(instructionSetClassName)
            .addSuperinterface(InstructionSet::class)
        type.addProperty(
            PropertySpec.builder(InstructionSet::memory.name, IntArray::class)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("%T(%L)", IntArray::class, dfis.instructions.size)
                .build()
        )
        val initializer = CodeBlock.builder()
        var index = 0
        var blockIndex = 0
        val instructions = dfis.instructions
        val total = instructions.size

        while (index < total) {
            val end = minOf(index + sizeLimit, total)
            val functionName = "init$blockIndex"
            val body = FunSpec.builder(functionName)

            while (index < end) {
                body.addStatement("%L[%L] = %L", InstructionSet::memory.name, index, instructions[index])
                index++
            }

            type.addFunction(body.build())
            initializer.addStatement("%L()", functionName)
            blockIndex++
        }
        type.addInitializerBlock(initializer.build())
        val file = FileSpec.builder(instructionSetClassName)
            .addType(type.build())
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                    .addMember(
                        "%S, %S",
                        "ClassName",
                        "RedundantVisibilityModifier"
                    )
                    .build()
            )
            .build()
        collector.emit(file)
    }
}
