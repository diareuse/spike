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

fun interface Generator {
    fun generate(context: FileGeneratorContext, collector: FileSpecCollector)
}

fun interface FileSpecCollector {
    fun emit(file: FileSpec)
}

data class FileGeneratorContext(
    val resolver: TypeResolver,
    val graph: DependencyGraph,
    val instructions: DependencyFactoryInstructionsSet = DependencyFactoryInstructionsSet(),
    val ids: TypeFactoryIdHolder = TypeFactoryIdHolder()
) {
    fun getDependencyId(factory: TypeFactory): Int {
        return ids.getOrAdd(factory)
    }
}

class EntryPointGenerator(
    private val dependencyFactoryClassName: ClassName
) : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val graph = context.graph
        val resolver = context.resolver
        val ep = graph.entry
        val epcn = resolver.peerClass(graph, "EntryPoint")
        val dfcn = dependencyFactoryClassName
        val type = TypeSpec.objectBuilder(epcn)
            .addSuperinterface(resolver.getTypeName(ep.type))
            .addModifiers(KModifier.PRIVATE)
        for (m in ep.methods) {
            type.addFunction(
                FunSpec.builder(m.name)
                    .addModifiers(KModifier.OVERRIDE)
                    .returns(resolver.getTypeName(m.returns))
                    .addStatement("return %T.get(%L(%L))", dfcn, DependencyId::class.asClassName(), context.getDependencyId(context.ids.find(m.returns)))
                    .build()
            )
        }
        for (p in ep.properties) {
            type.addProperty(
                PropertySpec.builder(p.name, resolver.getTypeName(p.returns))
                    .addModifiers(KModifier.OVERRIDE)
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return %T.get(%L(%L))", dfcn, DependencyId::class.asClassName(), context.getDependencyId(context.ids.find(p.returns)))
                            .build()
                    )
                    .build()
            )
        }
        val file = FileSpec.builder(epcn)
            .addType(type.build())
            .addFunction(
                FunSpec.builder("invoke")
                    .addModifiers(KModifier.OPERATOR)
                    .receiver((resolver.getTypeName(ep.type) as ClassName).nestedClass("Companion"))
                    .returns(resolver.getTypeName(ep.type))
                    .addStatement("return %T", epcn)
                    .build()
            )
            .build()
        collector.emit(file)
    }
}

class InstructionSetGenerator : Generator {
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
        val file = FileSpec.builder(instructionSetClassName)
            .addType(type.build())
            .build()
        collector.emit(file)
    }
}

class DependencyHolderGenerator(
    private val index: Int,
    private val dependencyFactoryClassName: ClassName
) : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val factories = context.ids.toList()[index]
        val className = context.resolver.peerClass(context.graph, "DependencyHolder${index}")
        val type = TypeSpec.objectBuilder(className)
        type.addFunction(context.run { createCreateMethod(factories) })
        val file = FileSpec.builder(className)
            .addType(type.build())
            .build()
        collector.emit(file)
    }

    context(context: FileGeneratorContext)
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
                    mapEntries(dependencyFactoryClassName, factory.keyValues.entries)
                }.addStatement("")
                is TypeFactory.Property -> error("Properties are unsupported, bind using external holder")
            }
        }
        body.addStatement("else -> error(\"Invalid position\")")
        body.endControlFlow()
        builder.addCode(body.build())

        return builder.build()
    }
}

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