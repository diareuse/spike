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
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSet
import spike.factory.InstructionSetPointer
import kotlin.reflect.KClass

class MegaGenerator(
    private val graph: DependencyGraph,
    private val resolver: TypeResolver
) {

    private val dependencyFactoryClassName = resolver.peerClass(graph, "Factory")
    private val instructionSetClassName = resolver.peerClass(graph, "InstructionSet")
    private val types = mutableListOf<FileSpec>()

    fun generate(): List<FileSpec> {
        types.clear()
        types += createDependencyFactory()
        types += createEntryPoint()
        return types.toList()
    }

    private fun createDependencyFactory(): FileSpec {
        val spec = TypeSpec.objectBuilder(dependencyFactoryClassName)
        spec.superclass(DependencyFactory::class)
        val factories = sequence {
            val queue = graph.iterator().asSequence().toList().toMutableList()
            while (queue.isNotEmpty()) {
                val item = queue.removeFirst()
                yield(item)
                queue.addAll(item.dependencies)
            }
        }
        val maxConstructorArgs = factories.maxOf { it.dependencies.size }
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

    class DependencyFactoryInstructionsSet {
        val instructions = mutableListOf<Int>()
        private var start = -1
        fun start() = instructions.size.also {
            start = it
        }

        fun end(contextSize: Int): Int {
            instructions.add(start, contextSize)
            return (instructions.size - start).also {
                start = -1
            }
        }

        fun add(instruction: Int) {
            instructions.add(instruction)
        }
    }

    private val dfis = DependencyFactoryInstructionsSet()
    private fun createGetInstructionsBody(graph: DependencyGraph): CodeBlock {
        val block = CodeBlock.builder()
            .beginControlFlow("return when (id.%L) {", DependencyId::id.name)
        val queue = graph.iterator().asSequence().toMutableList()
        val uniqueTypes = mutableSetOf<Int>()
        while (queue.isNotEmpty()) {
            val type = queue.removeFirst()
            if (!uniqueTypes.add(type.hashCode())) {
                continue
            }
            val visited = mutableSetOf<Int>()
            val contextWindow = mutableListOf<Int>()
            val dependencyTree = type.invertDependencyChain() + type
            block.add("%L -> ", getDependencyId(type))
            val offset = dfis.start()
            var contextSize = 0
            for (dependency in dependencyTree) {
                when (dependency) {
                    is TypeFactory.Deferred -> queue.add(0, dependency.factory)
                    is TypeFactory.MultibindsCollection -> queue.addAll(0, dependency.entries)
                    is TypeFactory.MultibindsMap -> queue.addAll(0, dependency.keyValues.values)
                    else -> {}
                }
                if (!visited.add(dependency.hashCode()))
                    continue
                contextWindow += dependency.hashCode()
                contextSize++
                val id = getDependencyId(dependency)
                dfis.add(id)
                val dependencyCount = if (dependency is TypeFactory.Deferred) 0 else dependency.dependencies.size
                dfis.add(dependencyCount)
                if (dependency !is TypeFactory.Deferred) for (argument in dependency.dependencies) {
                    val i = contextWindow.indexOf(argument.hashCode())
                    if (i == -1) error("Dependency '$argument' was not found in dependencyTree $dependencyTree")
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

    private fun createDependencyHolders(): List<Pair<Int, ClassName>> {
        return buildList {
            for ((index, holder) in tfih.holders.withIndex()) {
                add(index to createDependencyHolder(index, holder))
            }
        }
    }

    // ---

    private fun TypeFactory.invertDependencyChain(): List<TypeFactory> = buildList {
        val queue = dependencies.toMutableList()
        while (queue.isNotEmpty()) {
            val dependency = queue.removeFirst()
            if (dependency !is TypeFactory.Deferred) {
                queue.addAll(0, dependency.dependencies)
            }
            add(0, dependency)
        }
    }

    class TypeFactoryIdHolder {
        val holders = mutableListOf<MutableList<TypeFactory>>(mutableListOf())
        fun add(typeFactory: TypeFactory): Int {
            var h = holders.last()
            if (h.size >= 1000) { // keep under 1000 to retain JIT speed boosts
                holders.add(mutableListOf())
                h = holders.last()
            }
            val segment = holders.indexOf(h)
            val index = h.size
            h.add(typeFactory)
            return DependencyId(segment, index).id
        }

        fun getOrAdd(typeFactory: TypeFactory): Int {
            for ((segment, holder) in holders.withIndex()) {
                val index = holder.indexOf(typeFactory)
                if (index >= 0) {
                    return DependencyId(segment, index).id
                }
            }
            return add(typeFactory)
        }

        fun find(type: Type) = holders.flatten().first { it.type == type }
    }

    private val tfih = TypeFactoryIdHolder()
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

    private fun createCreateMethod(factories: List<TypeFactory>): FunSpec {
        val builder = FunSpec.builder("create")
            .addModifiers(KModifier.INTERNAL)
            .addParameter("buffer", Array::class.asTypeName().parameterizedBy(Any::class.asTypeName().copy(nullable = true)))
            .addParameter("position", Int::class)
            .returns(Any::class)

        val body = CodeBlock.builder()
        body.beginControlFlow("return when(position) {")
        // the index here expects a well-ordered factories argument
        for ((index, factory) in factories.withIndex()) {
            body.add("$index -> ")
            when (factory) {
                is TypeFactory.Binds -> body.addStatement("buffer[0] as %T", factory.type.toTypeName())
                is TypeFactory.Class -> {
                    body.add("%T(", factory.type.toTypeName())
                    for ((index, parameter) in factory.invocation.parameters.withIndex()) {
                        if (index > 0) body.add(", ")
                        body.add("buffer[%L] as %T", index, parameter.type.toTypeName())
                    }
                    body.addStatement(")")
                }
                is TypeFactory.Method -> {
                    body.add("%M(", resolver.getMemberName(factory.member))
                    for ((index, parameter) in factory.invocation.parameters.withIndex()) {
                        if (index > 0) body.add(", ")
                        body.add("buffer[%L] as %T", index, parameter.type.toTypeName())
                    }
                    body.addStatement(")")
                }
                is TypeFactory.Memorizes -> {
                    body.addStatement("%M { %T.get<%T>(%T(%L)) }", resolver.builtInMember { lazy }, dependencyFactoryClassName, factory.factory.type.toTypeName(), DependencyId::class, getDependencyId(factory.factory))
                }
                is TypeFactory.Provides -> {
                    body.addStatement("%T { %T.get<%T>(%T(%L)) }", resolver.builtInType { Provider }, dependencyFactoryClassName, factory.factory.type.toTypeName(), DependencyId::class, getDependencyId(factory.factory))
                }
                is TypeFactory.MultibindsCollection -> {
                    body.add("%M(", resolver.getMemberName(factory.collectionMemberFactory))
                    for ((index, item) in factory.entries.withIndex()) {
                        if (index > 0) body.add(", ")
                        body.add("buffer[%L] as %T", index, item.type.toTypeName())
                    }
                    body.addStatement(")")
                }
                is TypeFactory.MultibindsMap -> {
                    val t = factory.type as Type.Parametrized
                    val a = t.typeArguments
                    body.addStatement("%M<%T, %T>(", resolver.builtInMember { mapOf }, a[0].toTypeName(), a[1].toTypeName())
                    body.withIndent {
                        for ((index, entry) in factory.keyValues.entries.withIndex()) {
                            if (index > 0) body.addStatement(",")
                            val (k, v) = entry
                            val key = when (k) {
                                is String -> "\"$k\""
                                is KClass<*> -> "${k.qualifiedName}::class"
                                is Type -> resolver.getTypeName(k).toString() + "::class"
                                else -> k
                            }
                            body.add("%L to ", key)
                            when (v) {
                                is TypeFactory.Memorizes -> body.add("%M { %T.get<%T>(%T(%L)) }", resolver.builtInMember { lazy }, dependencyFactoryClassName, v.type.typeArguments.single().toTypeName(), DependencyId::class, getDependencyId(factory))
                                is TypeFactory.Provides -> body.add("%T { %T.get<%T>(%T(%L)) }", resolver.builtInType { Provider }, dependencyFactoryClassName, v.type.typeArguments.single().toTypeName(), DependencyId::class, getDependencyId(factory))
                                else -> body.add("buffer[%L] as %T", index, v.type.toTypeName())
                            }
                        }
                        body.addStatement("")
                    }
                    body.addStatement(")")
                }
                is TypeFactory.Property -> {
                    // fixme properties don't make any sense in this type of resolution-matching
                    //  - users might instead bind external class with lateinit property and initialize said property prior to calling the entry point
                    //    - this can also allow dynamic readjustments without reinitializing the graph
                    //body.add("buffer[0] as %T", factory.type.toClassName())
                    body.addStatement("error(\"Properties are deprecated, bind using external holder\")")
                }
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
        for ((no, block) in dfis.instructions.chunked(1000).withIndex()) {
            val body = FunSpec.builder("init$no")
            for (instruction in block) {
                body.addStatement("%L[%L] = %L", InstructionSet::memory.name, index++, instruction)
            }
            type.addFunction(body.build())
            initializer.addStatement("%L()", body.build().name)
        }
        type.addInitializerBlock(initializer.build())
        types += FileSpec.builder(instructionSetClassName)
            .addType(type.build())
            .build()
        return instructionSetClassName
    }

    // ---###---

    private fun createEntryPoint(): FileSpec {
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
                    .addStatement("return %T.get(%L(%L))", dfcn, DependencyId::class.asClassName(), getDependencyId(tfih.find(m.returns)))
                    .build()
            )
        }
        for (p in ep.properties) {
            type.addProperty(
                PropertySpec.builder(p.name, resolver.getTypeName(p.returns))
                    .addModifiers(KModifier.OVERRIDE)
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return %T.get(%L(%L))", dfcn, DependencyId::class.asClassName(), getDependencyId(tfih.find(p.returns)))
                            .build()
                    )
                    .build()
            )
        }
        return FileSpec.builder(epcn)
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
    }

}