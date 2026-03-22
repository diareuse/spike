package spike.compiler.generator

import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.withIndent
import spike.compiler.graph.BuiltInMembers
import spike.compiler.graph.BuiltInTypes
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

class MegaGenerator(
    private val graph: DependencyGraph,
    private val resolver: TypeResolver,
    private val logger: KSPLogger
) {

    private val types = mutableListOf<TypeSpec>()

    fun generate(): List<TypeSpec> {
        types.clear()
        types += createDependencyFactory()
        return types.toList()
    }

    private fun createDependencyFactory(): TypeSpec {
        val spec = TypeSpec.classBuilder("DependencyFactoryImpl")
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
            FunSpec.builder("getInstructions")
                .addModifiers(KModifier.OVERRIDE)
                .returns(IntArray::class.asClassName().copy(nullable = true))
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
        return spec.build()
    }

    // ---

    private fun createInstantiateBody(): CodeBlock {
        val dependencyHolders = createDependencyHolders()
        val block = CodeBlock.builder()
        block.beginControlFlow("return when (id.%L) {", DependencyId::segment.name)
        block.withIndent {
            for ((index, holder) in dependencyHolders) {
                addStatement("$index -> $holder.create(buffer, id.position)")
            }
            addStatement("else -> error(\"Invalid segment\")")
        }
        block.endControlFlow()
        return block.build()
    }

    private fun createGetInstructionsBody(graph: DependencyGraph): CodeBlock {
        val block = CodeBlock.builder()
            .beginControlFlow("return when (id.%L) {", DependencyId::id.name)
        for (type in graph.iterator()) {
            val visited = mutableSetOf<Int>()
            val contextWindow = mutableListOf<Int>()
            val dependencyTree = type.invertDependencyChain() + type
            block.add("%L -> intArrayOf(", getDependencyId(type))
            val deps = CodeBlock.builder()
            var contextSize = 0
            for (dependency in dependencyTree) {
                if (!visited.add(dependency.hashCode()))
                    continue
                contextWindow+=dependency.hashCode()
                contextSize++
                val id = getDependencyId(dependency)
                deps.add(",%L", id)
                val dependencyCount = dependency.dependencies.size
                deps.add(",%L", dependencyCount)
                for (argument in dependency.dependencies) {
                    val i = contextWindow.indexOf(argument.hashCode())
                    if (i == -1) error("Dependency '$argument' was not found in dependencyTree $dependencyTree")
                    deps.add(",%L", i)
                }
            }
            block.add("%L", contextSize)
            block.add(deps.build())
            block.addStatement(")")
        }
        block.addStatement("else -> error(\"Invalid identifier\")")
        block.endControlFlow()
        return block.build()
    }

    // ---

    private fun createDependencyHolders(): List<Pair<Int, String>> {
        return buildList {
            for((index, holder) in tfih.holders.withIndex()) {
                add(index to createDependencyHolder(index, holder).name!!) // fixme this should return ClassName
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
            if (h.size >= 8000) {
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
    }

    private val tfih = TypeFactoryIdHolder()
    private fun getDependencyId(factory: TypeFactory): Int {
        return tfih.getOrAdd(factory)
    }

    // ---###---

    private fun createDependencyHolder(index: Int, factories: List<TypeFactory>): TypeSpec {
        val type = TypeSpec.objectBuilder("DependencyHolder$index")
        type.addFunction(createCreateMethod(factories))
        return type.build().also { type ->
            types += type
        }
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
                is TypeFactory.Binds -> body.addStatement("buffer[0] as %T", factory.type.toClassName())
                is TypeFactory.Class -> {
                    body.add("%T(", factory.type.toClassName())
                    for ((index, parameter) in factory.invocation.parameters.withIndex()) {
                        if (index > 0) body.add(", ")
                        body.add("buffer[%L] as %T", index, parameter.type.toClassName())
                    }
                    body.addStatement(")")
                }
                is TypeFactory.Method -> {
                    body.add("%M(", resolver.getMemberName(factory.member))
                    for ((index, parameter) in factory.invocation.parameters.withIndex()) {
                        if (index > 0) body.add(", ")
                        body.add("buffer[%L] as %T", index, parameter.type.toClassName())
                    }
                    body.addStatement(")")
                }
                is TypeFactory.Memorizes -> {
                    // fixme support for lazy should be probably removed, it currently makes no sense
                    body.addStatement("%M { buffer[0] as %T }", BuiltInMembers.lazy, factory.type.toClassName())
                }
                is TypeFactory.Provides -> {
                    // fixme support for providers should be probably removed or re-thought
                    //  - to overcome circular dependency chain, users should switch to calling via method
                    //  - to provide new instances, users should create explicit factories and not to depend on DI creating them for them
                    //    - actually we already kind of have factories through `DH.create(buffer, position)` so wrapping these should suffice(?)
                    //    - we would need to know which dependency holder owns it to create it from here
                    body.addStatement("%T { buffer[0] as %T }", BuiltInTypes.Provider, factory.type.toClassName())
                }
                is TypeFactory.MultibindsCollection -> {
                    body.add("%M(", resolver.getMemberName(factory.collectionMemberFactory))
                    for ((index, item) in factory.entries.withIndex()) {
                        if (index > 0) body.add(", ")
                        body.add("buffer[%L] as %T", index, item.type.toClassName())
                    }
                    body.addStatement(")")
                }
                is TypeFactory.MultibindsMap -> {
                    body.add("%M(", resolver.builtInMember { mapOf })
                    for ((index, entry) in factory.keyValues.entries.withIndex()) {
                        if (index > 0) body.add(", ")
                        val (k, v) = entry
                        val key = when (k) {
                            is String -> "\"$k\""
                            is KClass<*> -> "${k.qualifiedName}::class"
                            is Type -> resolver.getTypeName(k).toString() + "::class"
                            else -> k
                        }
                        // fixme this is the most certainly completely wrong and will mismatch instances to indices
                        body.add("%L to buffer[%L] as %T", key, index, v.type.toClassName())
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

}