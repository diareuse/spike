package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.withIndent
import spike.compiler.graph.TypeFactory
import spike.compiler.graph.TypeFactory.Companion.dependencyTree
import spike.compiler.graph.TypeFactory.Companion.invertDependencyTree
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSet
import spike.factory.InstructionSetPointer

class DependencyFactoryGenerator(
    private val dependencyFactoryClassName: ClassName,
    private val instructionSet: InstructionSetGenerator,
    private val dependencyHolder: (Int) -> DependencyHolderGenerator,
) : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val spec = TypeSpec.objectBuilder(dependencyFactoryClassName)
        spec.superclass(DependencyFactory::class)
        val graph = context.graph
        val maxConstructorArgs = graph.toSequence().flatMap { it.dependencyTree() }.maxOf { it.dependencies.size }
        spec.addProperty(
            PropertySpec.builder("maxConstructorArgs", Int::class)
                .initializer("$maxConstructorArgs")
                .addModifiers(KModifier.OVERRIDE)
                .build()
        )
        // ---
        spec.addFunction(
            FunSpec.builder("getInstructionsPointer")
                .addModifiers(KModifier.OVERRIDE)
                .returns(InstructionSetPointer::class.asClassName().copy(nullable = true))
                .addParameter("id", DependencyId::class)
                .addCode(createGetInstructionsBody(context))
                .build()
        )
        spec.addFunction(
            FunSpec.builder("instantiate")
                .addModifiers(KModifier.OVERRIDE)
                .returns(Any::class)
                .addParameter("buffer", Array::class.asClassName().parameterizedBy(Any::class.asTypeName().copy(nullable = true)))
                .addParameter("id", DependencyId::class)
                .addCode(createInstantiateBody(context, collector))
                .build()
        )
        spec.addProperty(
            PropertySpec.builder("instructionSet", IntArray::class)
                .addModifiers(KModifier.OVERRIDE)
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return %T.%L", createInstructionSet(context, collector), InstructionSet::memory.name)
                        .build()
                )
                .build()
        )
        val type = spec.build()
        val file = FileSpec.builder(dependencyFactoryClassName)
            .addType(type)
            .build()
        collector.emit(file)
    }

    private fun createInstantiateBody(context: FileGeneratorContext, collector: FileSpecCollector): CodeBlock {
        val block = CodeBlock.builder()
        block.beginControlFlow("return when (id.%L) {", DependencyId::segment.name)
        block.withIndent {
            for (index in context.ids.indices) {
                val holder = buildList {
                    dependencyHolder(index).generate(context) {
                        collector.emit(it)
                        add(it)
                    }
                }.single().toClassName()
                addStatement("$index -> %T.create(buffer, id.position)", holder)
            }
            addStatement("else -> error(\"Invalid segment\")")
        }
        block.endControlFlow()
        return block.build()
    }

    private fun createGetInstructionsBody(context: FileGeneratorContext): CodeBlock {
        val graph = context.graph
        val block = CodeBlock.builder()
            .beginControlFlow("return when (id.%L) {", DependencyId::id.name)
        val queue = ArrayDeque(graph.toSequence().toList())
        val uniqueTypes = HashSet<Int>()
        while (queue.isNotEmpty()) {
            val type = queue.removeFirst()
            if (!uniqueTypes.add(type.hashCode())) {
                continue
            }
            val contextIndexByHash = HashMap<Int, Int>()
            block.add("%L -> ", context.getDependencyId(type))
            val offset = context.instructions.start()
            var contextSize = 0
            for (dependency in type.invertDependencyTree()) {
                when (dependency) {
                    is TypeFactory.Deferred -> queue.add(0, dependency.factory)
                    is TypeFactory.MultibindsCollection -> queue.addAll(0, dependency.entries)
                    is TypeFactory.MultibindsMap -> queue.addAll(0, dependency.keyValues.values)
                    else -> {}
                }
                val dependencyHash = dependency.hashCode()
                if (contextIndexByHash.containsKey(dependencyHash))
                    continue
                contextIndexByHash[dependencyHash] = contextSize++
                val id = context.getDependencyId(dependency)
                context.instructions.add(id)
                val dependencyCount = if (dependency is TypeFactory.Deferred) 0 else dependency.dependencies.size
                context.instructions.add(dependencyCount)
                if (dependency !is TypeFactory.Deferred) for (argument in dependency.dependencies) {
                    val i = contextIndexByHash[argument.hashCode()]
                        ?: error("Dependency '$argument' was not found in dependencyTree")
                    context.instructions.add(i)
                }
            }
            val size = context.instructions.end(contextSize)
            block.addStatement("%T(%L, %L)", InstructionSetPointer::class.asClassName(), offset, size)
        }
        block.addStatement($$"else -> error(\"Invalid identifier ${id}\")")
        block.endControlFlow()
        return block.build()
    }

    private fun createInstructionSet(context: FileGeneratorContext, collector: FileSpecCollector): ClassName {
        return buildList {
            instructionSet.generate(context) {
                collector.emit(it)
                add(it)
            }
        }.single().toClassName()
    }
}