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
import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory
import spike.compiler.graph.TypeFactory.Companion.dependencyTree
import spike.compiler.graph.TypeFactory.Companion.invertDependencyTree
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSet
import spike.factory.InstructionSetPointer
import kotlin.reflect.KClass

class MegaGenerator(
    private val context: FileGeneratorContext,
    private val entryPoint: EntryPointGenerator
) {
    private val graph: DependencyGraph inline get() = context.graph
    private val resolver: TypeResolver inline get() = context.resolver

    private val dependencyFactoryClassName = resolver.peerClass(graph, "Factory")
    private val instructionSetClassName = resolver.peerClass(graph, "InstructionSet")
    private val dfis inline get() = context.instructions
    private val tfih inline get() = context.ids

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
        val dependencyHolders = createDependencyHolders()
        val block = CodeBlock.builder()
        block.beginControlFlow("return when (id.%L) {", DependencyId::segment.name)
        block.withIndent {
            for ((index, holder) in dependencyHolders) {
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

    private fun createDependencyHolders() = tfih.toList().mapIndexed { index, holder ->
        index to createDependencyHolder(index, holder)
    }

    // ---
    private fun getDependencyId(factory: TypeFactory): Int {
        return tfih.getOrAdd(factory)
    }

    // ---###---

    private fun createDependencyHolder(index: Int, factories: List<TypeFactory>): ClassName {
        val className = resolver.peerClass(graph, "DependencyHolder$index")
        val type = TypeSpec.objectBuilder(className)
        type.addFunction(createCreateMethod(factories))
        type.build().also { type ->
            types += FileSpec.builder(className)
                .addType(type)
                .build()
        }
        return className
    }

    private fun mapEntryKey(key: Any?) = when (key) {
        is String -> "\"$key\""
        is KClass<*> -> "${key.qualifiedName}::class"
        is Type -> resolver.getTypeName(key).toString() + "::class"
        else -> key
    }

    private fun CodeBlock.Builder.mapEntries(entries: Iterable<Map.Entry<Any?, TypeFactory>>) = apply {
        for ((index, entry) in entries.withIndex()) context.apply {
            if (index > 0) addStatement(",")
            val (k, v) = entry
            add("%L to ", mapEntryKey(k))
            when (v) {
                is TypeFactory.Memorizes -> addLazy {
                    addDependencyFactoryCall(dependencyFactoryClassName, v.factory, v.type.typeArguments.single())
                }
                is TypeFactory.Provides -> addProvider {
                    addDependencyFactoryCall(dependencyFactoryClassName, v.factory, v.type.typeArguments.single())
                }
                else -> addBufferCast(index, v.type)
            }
        }
        addStatement("")
    }

    private fun createCreateMethod(factories: List<TypeFactory>): FunSpec {
        val builder = FunSpec.builder("create")
            .addModifiers(KModifier.INTERNAL)
            .addParameter("buffer", Array::class.asTypeName().parameterizedBy(Any::class.asTypeName().copy(nullable = true)))
            .addParameter("position", Int::class)
            .returns(Any::class)

        val body = CodeBlock.builder()
        body.beginControlFlow("return when(position) {")
        // the index here expects a well-ordered factories argument
        for ((index, factory) in factories.withIndex()) context.apply {
            body.add("$index -> ")
            when (factory) {
                is TypeFactory.Binds -> body.addBufferCast(0, factory.type).addStatement("")
                is TypeFactory.Class -> body.addType(factory.type) {
                    body.addParameters(factory.invocation)
                }.addStatement("")
                is TypeFactory.Method -> body.addMember(factory.member) {
                    body.addParameters(factory.invocation)
                }.addStatement("")
                is TypeFactory.Memorizes -> body.addLazy {
                    addDependencyFactoryCall(dependencyFactoryClassName, factory.factory)
                }.addStatement("")
                is TypeFactory.Provides -> body.addProvider {
                    addDependencyFactoryCall(dependencyFactoryClassName, factory.factory)
                }.addStatement("")
                is TypeFactory.MultibindsCollection -> body.addMember(factory.collectionMemberFactory) {
                    addParameters(factory.entries)
                }.addStatement("")
                is TypeFactory.MultibindsMap -> body.addMap(factory.type.typeArguments[0], factory.type.typeArguments[1]) {
                    mapEntries(factory.keyValues.entries)
                }.addStatement("")
                is TypeFactory.Property -> error("Properties are unsupported, bind using external holder")
            }
        }
        body.addStatement("else -> error(\"Invalid position\")")
        body.endControlFlow()
        builder.addCode(body.build())

        return builder.build()
    }

    // ---

    private fun createInstructionSet(): ClassName {
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
            val end = minOf(index + 1000, total)
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
        types += FileSpec.builder(instructionSetClassName)
            .addType(type.build())
            .build()
        return instructionSetClassName
    }

}