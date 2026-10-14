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
import com.squareup.kotlinpoet.TypeVariableName
import spike.Include
import spike.compiler.graph.TypeFactory
import spike.compiler.graph.TypeFactory.Companion.invertDependencyTree
import spike.factory.DependencyId
import spike.factory.SingletonHolder

@Include
class DependencyFactoryGenerator(
    private val programs: LinearProgramGenerator,
) : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val spec = TypeSpec.classBuilder(context.dependencyFactoryClassName)
            .addOriginatingFiles(context.originatingFiles)

        context.imports.clear()
        val primaryConstructor = FunSpec.constructorBuilder()

        addEntryFactoryProperties(spec, primaryConstructor, context)
        createGet(context, spec, collector)
        addImportProperties(spec, context)
        addProviderProperties(spec, primaryConstructor, context)
        primaryConstructor.addParameter(
            ParameterSpec.builder("singletons", SingletonHolder::class)
                .defaultValue("%T()", SingletonHolder::class)
                .build()
        )
        spec.addProperty(
            PropertySpec.builder("singletons", SingletonHolder::class)
                .initializer("singletons")
                .build()
        )

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
                        import.invocation.parameters.forEach { param ->
                            add(
                                "%L = get(%T(%L)), ",
                                param.name,
                                DependencyId::class,
                                context.getDependencyId(context.graph.importedFactoryOf(param.type))
                            )
                        }
                        add("singletons = singletons.scope(%S)", cn.canonicalName)
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
            val name = import.name
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

    private fun createGet(
        context: FileGeneratorContext,
        spec: TypeSpec.Builder,
        collector: FileSpecCollector
    ) {
        val typeVariable = TypeVariableName("T")
        spec.addFunction(
            FunSpec.builder("get")
                .addAnnotation(
                    AnnotationSpec.builder(Suppress::class).addMember("%S", "UNCHECKED_CAST").build()
                )
                .addTypeVariable(typeVariable)
                .returns(typeVariable)
                .addParameter("id", DependencyId::class)
                .addCode(createGetBody(context, collector))
                .build()
        )
    }

    private fun createGetBody(context: FileGeneratorContext, collector: FileSpecCollector): CodeBlock {
        val block = CodeBlock.builder()
            .beginControlFlow("return when (id.%L) {", DependencyId::id.name)
        val queue = ArrayDeque(context.graph.toSequence().toList())
        val uniqueTypes = HashSet<TypeFactory>()

        while (queue.isNotEmpty()) {
            val type = queue.removeFirst()
            if (!uniqueTypes.add(type)) {
                continue
            }
            val id = context.getDependencyId(type)
            val program = createProgram(type, queue, context)
            val className = programs.generate(context, program, uniqueTypes.size - 1, collector)
            block.addStatement("%L -> %T.resolve(this)", id, className)
        }

        block.addStatement($$"else -> error(\"Invalid identifier $id\")")
        block.unindent().add("} as T\n")
        return block.build()
    }

    private fun createProgram(
        type: TypeFactory,
        queue: ArrayDeque<TypeFactory>,
        context: FileGeneratorContext,
    ): LinearProgram {
        val contextIndexByFactory = HashMap<TypeFactory, Int>()
        val steps = mutableListOf<LinearStep>()

        for (dependency in type.invertDependencyTree()) {
            if (dependency is TypeFactory.Imported) {
                context.imports += dependency
            }
            enqueueNestedTypes(dependency, queue)

            if (dependency in contextIndexByFactory) {
                continue
            }

            steps += LinearStep(dependency, argumentsOf(dependency, contextIndexByFactory))
            contextIndexByFactory[dependency] = steps.lastIndex
        }

        return LinearProgram(steps)
    }

    private fun argumentsOf(dependency: TypeFactory, contextIndexByFactory: Map<TypeFactory, Int>): List<Int> {
        if (dependency is TypeFactory.Deferred) return emptyList()
        return dependency.dependencies.map { argument ->
            contextIndexByFactory[argument] ?: error("Dependency '$argument' was not found in dependencyTree")
        }
    }

    private fun enqueueNestedTypes(dependency: TypeFactory, queue: ArrayDeque<TypeFactory>) {
        when (dependency) {
            is TypeFactory.Deferred -> queue.add(0, dependency.factory)
            is TypeFactory.MultibindsCollection -> queue.addAll(0, dependency.entries)
            is TypeFactory.MultibindsMap -> queue.addAll(0, dependency.keyValues.values)
            else -> {}
        }
    }
}
