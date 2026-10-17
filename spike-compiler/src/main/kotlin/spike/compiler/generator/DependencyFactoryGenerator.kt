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
import com.squareup.kotlinpoet.asTypeName
import spike.Include
import spike.compiler.graph.TypeFactory
import spike.compiler.graph.TypeFactory.Companion.invertDependencyTree
import spike.factory.DependencyId
import spike.factory.SingletonHolder
import spike.factory.SingletonOwner

@Include
class DependencyFactoryGenerator(
    private val programs: LinearProgramGenerator,
    @PlainBranches private val getDispatch: DispatchFunctions,
    @AssistedBranches private val assistedDispatch: DispatchFunctions,
) {

    fun generate(context: FileGeneratorContext, collector: FileSpecCollector, members: EntryPointMembers) {
        val spec = TypeSpec.classBuilder(context.dependencyFactoryClassName)
            .addOriginatingFiles(context.originatingFiles)

        context.imports.clear()
        val primaryConstructor = FunSpec.constructorBuilder()

        addEntryFactoryProperties(spec, primaryConstructor, context)
        createGet(context, spec, collector)
        addImportProperties(spec, context)
        addProviderProperties(spec, primaryConstructor, context)
        primaryConstructor.addParameter(
            ParameterSpec.builder("singletons", SingletonHolder::class.asTypeName().copy(nullable = true))
                .defaultValue("null")
                .build()
        )
        spec.superclass(SingletonOwner::class)
        spec.addSuperclassConstructorParameter("singletons")
        members.addTo(spec, context)

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
            parameters.map { ParameterSpec.builder("_${it.name}", context.resolver.getTypeName(it.type)).build() }
        )
        spec.addProperties(
            parameters.map {
                PropertySpec.builder("_${it.name}", context.resolver.getTypeName(it.type))
                    .initializer("_${it.name}")
                    .addModifiers(KModifier.INTERNAL)
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
                        add("singletons = _singletons.scope(%S)", cn.canonicalName)
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
            val type = context.resolver.getTypeName(import.type)
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
        val plain = BranchQueue(takesArguments = false, root = { it })
        val assisted = BranchQueue(takesArguments = true, root = { (it as TypeFactory.Assisted).factory })
        plain.pending += context.graph.toSequence()
        var index = 0
        var queue = listOf(plain, assisted).firstOrNull { it.pending.isNotEmpty() }
        while (queue != null) {
            val type = queue.pending.removeFirst()
            if (queue.seen.add(type)) {
                val id = context.getDependencyId(type)
                val program = createProgram(queue.root(type), plain, assisted, context, queue.takesArguments)
                queue.branches += ProgramBranch(id, programs.generate(context, program, index++, collector))
            }
            queue = listOf(plain, assisted).firstOrNull { it.pending.isNotEmpty() }
        }
        spec.addFunctions(assistedDispatch.of(assisted.branches))
        spec.addFunctions(getDispatch.of(plain.branches))
    }

    /**
     * Dependencies waiting for a program, built by [root] and taking runtime arguments when [takesArguments].
     */
    private class BranchQueue(val takesArguments: Boolean, val root: (TypeFactory) -> TypeFactory) {
        val pending = ArrayDeque<TypeFactory>()
        val seen = HashSet<TypeFactory>()
        val branches = mutableListOf<ProgramBranch>()
    }

    private fun createProgram(
        type: TypeFactory,
        plain: BranchQueue,
        assisted: BranchQueue,
        context: FileGeneratorContext,
        takesArguments: Boolean,
    ): LinearProgram {
        val contextIndexByFactory = HashMap<TypeFactory, Int>()
        val steps = mutableListOf<LinearStep>()

        for (dependency in type.invertDependencyTree()) {
            if (dependency is TypeFactory.Imported) {
                context.imports += dependency
            }
            if (dependency is TypeFactory.Assisted) assisted.pending += dependency
            if (dependency is TypeFactory.Deferred) plain.pending.addFirst(dependency.factory.resolvedTarget)

            if (dependency in contextIndexByFactory) {
                continue
            }

            steps += LinearStep(dependency, argumentsOf(dependency, contextIndexByFactory))
            contextIndexByFactory[dependency] = steps.lastIndex
        }

        return LinearProgram(steps, takesArguments)
    }

    private fun argumentsOf(dependency: TypeFactory, contextIndexByFactory: Map<TypeFactory, Int>): List<Int> {
        if (dependency is TypeFactory.Deferred) return emptyList()
        return dependency.dependencies.map { argument ->
            contextIndexByFactory[argument] ?: error("Dependency '$argument' was not found in dependencyTree")
        }
    }

}
