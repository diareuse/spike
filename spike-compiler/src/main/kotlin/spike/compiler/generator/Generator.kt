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
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.TypeFactory
import spike.factory.DependencyId
import spike.factory.InstructionSet

fun interface Generator {
    fun generate(context: FileGeneratorContext): FileSpec
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
    override fun generate(context: FileGeneratorContext): FileSpec {
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

class InstructionSetGenerator : Generator {
    override fun generate(context: FileGeneratorContext): FileSpec {
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
        return FileSpec.builder(instructionSetClassName)
            .addType(type.build())
            .build()
    }
}

class DependencyHolderGenerator(
    private val index: Int,
    private val dependencyFactoryClassName: ClassName
) : Generator {
    override fun generate(context: FileGeneratorContext): FileSpec {
        val factories = context.ids.toList()[index]
        val className = context.resolver.peerClass(context.graph, "DependencyHolder${index}")
        val type = TypeSpec.objectBuilder(className)
        type.addFunction(context.run { createCreateMethod(factories) })
        return FileSpec.builder(className)
            .addType(type.build())
            .build()
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