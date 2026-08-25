package spike.compiler.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.withIndent
import spike.Include
import spike.compiler.graph.TypeFactory
import spike.compiler.graph.TypeFactory.Companion.dependencyTree
import spike.compiler.graph.TypeFactory.Companion.invertDependencyTree
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSet
import spike.factory.InstructionSetPointer

@Suppress("TooManyFunctions")
@Include
class DependencyFactoryGenerator(
    private val instructionSet: InstructionSetGenerator,
    private val holderFactory: DependencyHolderGenerator.Factory
) : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val spec = TypeSpec.classBuilder(context.dependencyFactoryClassName)
            .addOriginatingFiles(context.originatingFiles)
            .superclass(DependencyFactory::class)

        context.imports.clear()
        val primaryConstructor = FunSpec.constructorBuilder()

        addEntryFactoryProperties(spec, primaryConstructor, context)
        addOverrideProperties(spec, context)
        createInstructionPointer(context, spec)
        createInstantiate(context, spec, collector)
        addInstructionSetProperty(spec, context, collector)
        addImportProperties(spec, context)
        addProviderProperties(spec, primaryConstructor, context)

        spec.primaryConstructor(primaryConstructor.build())

        val file = FileSpec.builder(context.dependencyFactoryClassName)
            .addType(spec.build())
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                    .addMember("%S, %S", "ClassName", "RedundantVisibilityModifier")
                    .build()
            )
            .build()
        collector.emit(file)
    }

    private fun addEntryFactoryProperties(
        spec: TypeSpec.Builder,
        primaryConstructor: FunSpec.Builder,
        context: FileGeneratorContext
    ) {
        val parameters = context.graph.entry.factory.method.parameters
        primaryConstructor.addParameters(
            parameters.map { ParameterSpec.builder(it.name, context.resolver.getTypeName(it.type)).build() }
        )
        spec.addProperties(
            parameters.map {
                PropertySpec.builder(it.name, context.resolver.getTypeName(it.type))
                    .initializer(it.name)
                    .addModifiers(KModifier.PUBLIC)
                    .build()
            }
        )
    }

    private fun addOverrideProperties(spec: TypeSpec.Builder, context: FileGeneratorContext) {
        val maxConstructorArgs = context.graph.toSequence()
            .flatMap { it.dependencyTree() }
            .maxOf { it.dependencies.size }

        spec.addProperty(
            PropertySpec.builder("maxConstructorArgs", Int::class)
                .initializer("$maxConstructorArgs")
                .addModifiers(KModifier.OVERRIDE)
                .build()
        )
    }

    private fun addInstructionSetProperty(
        spec: TypeSpec.Builder,
        context: FileGeneratorContext,
        collector: FileSpecCollector
    ) {
        val instructionSetClassName = createInstructionSet(context, collector)
        spec.addProperty(
            PropertySpec.builder("instructionSet", IntArray::class)
                .addModifiers(KModifier.OVERRIDE)
                .getter(FunSpec.getterBuilder()
                    .addStatement("return %T.%L", instructionSetClassName, InstructionSet::memory.name)
                    .build())
                .build()
        )
    }

    private fun addImportProperties(spec: TypeSpec.Builder, context: FileGeneratorContext) {
        spec.addProperties(context.graph.imports.map { import ->
            val cn = import.type.toClassName()
            PropertySpec.builder(
                import.type.simpleName.replaceFirstChar { it.lowercase() },
                cn
            ).initializer(
                CodeBlock.builder()
                    .add("%T(", cn)
                    .apply {
                        import.invocation.parameters.forEachIndexed { index, param ->
                            if (index > 0) add(", ")
                            add(
                                "%L = get(%T(%L))",
                                param.name,
                                DependencyId::class,
                                context.getDependencyId(context.ids.find(param.type))
                            )
                        }
                    }
                    .add(")")
                    .build()
            ).build()
        })
    }

    private fun addProviderProperties(
        spec: TypeSpec.Builder,
        primaryConstructor: FunSpec.Builder,
        context: FileGeneratorContext
    ) {
        for (import in context.imports) {
            val name = context.resolver.getVariableName(import.type)
            val type = import.type.toClassName()
            val providerType = (context.resolver.builtInType { Provider } as ClassName).parameterizedBy(type)

            primaryConstructor.addParameter("_$name", providerType)
            spec.addProperty(
                PropertySpec.builder("_$name", providerType, KModifier.PRIVATE)
                    .initializer("_$name")
                    .build()
            )
            spec.addProperty(
                PropertySpec.builder(name, type, KModifier.PUBLIC)
                    .getter(FunSpec.getterBuilder()
                        .addCode("return _%L.get()", name)
                        .build())
                .build()
            )
        }
    }

    private fun createInstructionPointer(
        context: FileGeneratorContext,
        spec: TypeSpec.Builder
    ) {
        spec.addFunction(
            FunSpec.builder("getInstructionsPointer")
                .addModifiers(KModifier.OVERRIDE)
                .returns(InstructionSetPointerNull)
                .addParameter("id", DependencyId::class)
                .addCode(createGetInstructionsBody(context))
                .build()
        )
    }

    private fun createInstantiate(
        context: FileGeneratorContext,
        spec: TypeSpec.Builder,
        collector: FileSpecCollector
    ) {
        spec.addFunction(
            FunSpec.builder("instantiate")
                .addModifiers(KModifier.OVERRIDE)
                .returns(Any::class)
                .addParameter("buffer", ArrayOfAny)
                .addParameter("id", DependencyId::class)
                .addCode(createInstantiateBody(context, spec, collector))
                .build()
        )
    }

    private fun createInstantiateBody(
        context: FileGeneratorContext,
        spec: TypeSpec.Builder,
        collector: FileSpecCollector
    ): CodeBlock {
        val block = CodeBlock.builder()
        block.beginControlFlow("return when (id.%L) {", DependencyId::segment.name)
        block.withIndent {
            for (index in context.ids.indices) {
                val holder = buildList {
                    holderFactory.create(index).generate(context) {
                        collector.emit(it)
                        add(it)
                    }
                }.single().toClassName()
                spec.addProperty(
                    PropertySpec.builder("holder$index", holder)
                        .initializer("%T(this)", holder)
                        .addModifiers(KModifier.PRIVATE)
                        .build()
                )
                addStatement("$index -> holder$index.create(buffer, id.position)")
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

            block.add("%L -> ", context.getDependencyId(type))
            val offset = context.instructions.start()
            val contextSize = writeDependencies(
                type = type,
                queue = queue,
                context = context
            )
            val size = context.instructions.end(contextSize)
            block.addStatement("%T(%L, %L)", InstructionSetPointer::class.asClassName(), offset, size)
        }

        block.addStatement($$"else -> error(\"Invalid identifier $id\")")
        block.endControlFlow()
        return block.build()
    }

    private fun writeDependencies(
        type: TypeFactory,
        queue: ArrayDeque<TypeFactory>,
        context: FileGeneratorContext,
    ): Int {
        val contextIndexByHash = HashMap<Int, Int>()
        var contextSize = 0

        for (dependency in type.invertDependencyTree()) {
            if (dependency is TypeFactory.Imported) {
                context.imports += dependency
            }
            enqueueNestedTypes(dependency, queue)

            val dependencyHash = dependency.hashCode()
            if (contextIndexByHash.containsKey(dependencyHash)) {
                continue
            }

            contextIndexByHash[dependencyHash] = contextSize++
            appendDependencyInstructions(dependency, contextIndexByHash, context)
        }

        return contextSize
    }

    private fun enqueueNestedTypes(dependency: TypeFactory, queue: ArrayDeque<TypeFactory>) {
        when (dependency) {
            is TypeFactory.Deferred -> queue.add(0, dependency.factory)
            is TypeFactory.MultibindsCollection -> queue.addAll(0, dependency.entries)
            is TypeFactory.MultibindsMap -> queue.addAll(0, dependency.keyValues.values)
            else -> {}
        }
    }

    private fun appendDependencyInstructions(
        dependency: TypeFactory,
        contextIndexByHash: Map<Int, Int>,
        context: FileGeneratorContext,
    ) {
        val id = context.getDependencyId(dependency)
        context.instructions.add(id)

        val dependencyCount = if (dependency is TypeFactory.Deferred) 0 else dependency.dependencies.size
        context.instructions.add(dependencyCount)

        if (dependency !is TypeFactory.Deferred) {
            for (argument in dependency.dependencies) {
                val i = contextIndexByHash[argument.hashCode()]
                    ?: error("Dependency '$argument' was not found in dependencyTree")
                context.instructions.add(i)
            }
        }
    }


    private fun createInstructionSet(context: FileGeneratorContext, collector: FileSpecCollector): ClassName {
        return buildList {
            instructionSet.generate(context) {
                collector.emit(it)
                add(it)
            }
        }.single().toClassName()
    }

    private companion object {
        val InstructionSetPointerNull = InstructionSetPointer::class.asClassName().copy(nullable = true)
        val ArrayOfAny = Array::class.asClassName()
            .parameterizedBy(Any::class.asTypeName().copy(nullable = true))
    }
}
