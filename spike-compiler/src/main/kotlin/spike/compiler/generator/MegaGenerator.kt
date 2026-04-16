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
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.TypeFactory
import spike.compiler.graph.TypeFactory.Companion.dependencyTree
import spike.compiler.graph.TypeFactory.Companion.invertDependencyTree
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSet
import spike.factory.InstructionSetPointer

class MegaGenerator(
    private val context: FileGeneratorContext,
    private val entryPoint: EntryPointGenerator,
    private val instructionSet: InstructionSetGenerator,
    private val dependencyHolder: (Int) -> DependencyHolderGenerator,
) {
    private val graph: DependencyGraph inline get() = context.graph
    private val resolver: TypeResolver inline get() = context.resolver

    private val dependencyFactoryClassName = resolver.peerClass(graph, "Factory")
    private val dfis inline get() = context.instructions

    private val types = mutableListOf<FileSpec>()

    fun generate(): List<FileSpec> {
        types.clear()
        types += createDependencyFactory()
        types += entryPoint.generate(context)
        return types.toList()
    }

    private fun createDependencyFactory(): FileSpec {
        val spec = TypeSpec.objectBuilder(dependencyFactoryClassName)
        spec.superclass(DependencyFactory::class)
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
                .addCode(createGetInstructionsBody(graph))
                .build()
        )
        spec.addFunction(
            FunSpec.builder("instantiate")
                .addModifiers(KModifier.OVERRIDE)
                .returns(Any::class)
                .addParameter("buffer", Array::class.asClassName().parameterizedBy(Any::class.asTypeName().copy(nullable = true)))
                .addParameter("id", DependencyId::class)
                .addCode(createInstantiateBody())
                .build()
        )
        spec.addProperty(
            PropertySpec.builder("instructionSet", IntArray::class)
                .addModifiers(KModifier.OVERRIDE)
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return %T.%L", createInstructionSet(), InstructionSet::memory.name)
                        .build()
                )
                .build()
        )
        val type = spec.build()
        return FileSpec.builder(dependencyFactoryClassName)
            .addType(type)
            .build()
    }

    // ---
    private fun createInstantiateBody(): CodeBlock {
        val block = CodeBlock.builder()
        block.beginControlFlow("return when (id.%L) {", DependencyId::segment.name)
        block.withIndent {
            for (index in context.ids.indices) {
                val holder = dependencyHolder(index).generate(context).also { types += it }.toClassName()
                addStatement("$index -> %T.create(buffer, id.position)", holder)
            }
            addStatement("else -> error(\"Invalid segment\")")
        }
        block.endControlFlow()
        return block.build()
    }

    private fun createGetInstructionsBody(graph: DependencyGraph): CodeBlock {
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
            block.add("%L -> ", getDependencyId(type))
            val offset = dfis.start()
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
                val id = getDependencyId(dependency)
                dfis.add(id)
                val dependencyCount = if (dependency is TypeFactory.Deferred) 0 else dependency.dependencies.size
                dfis.add(dependencyCount)
                if (dependency !is TypeFactory.Deferred) for (argument in dependency.dependencies) {
                    val i = contextIndexByHash[argument.hashCode()]
                        ?: error("Dependency '$argument' was not found in dependencyTree")
                    dfis.add(i)
                }
            }
            val size = dfis.end(contextSize)
            block.addStatement("%T(%L, %L)", InstructionSetPointer::class.asClassName(), offset, size)
        }
        block.addStatement($$"else -> error(\"Invalid identifier ${id}\")")
        block.endControlFlow()
        return block.build()
    }

    // ---
    private fun getDependencyId(factory: TypeFactory): Int = context.getDependencyId(factory)

    // ---###---

    private fun createInstructionSet(): ClassName {
        return instructionSet.generate(context).also { types += it }.toClassName()
    }

}