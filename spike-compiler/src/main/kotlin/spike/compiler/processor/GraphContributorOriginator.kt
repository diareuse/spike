package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.writeTo
import spike.Export
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.GraphEntryPoint
import spike.compiler.graph.GraphEntryPoint.Companion.virtualFactory
import spike.compiler.graph.Member
import spike.compiler.graph.Parameter
import spike.compiler.graph.TypeFactory

@OptIn(KspExperimental::class)
abstract class GraphContributorOriginator : GraphContributor {
    protected abstract val generator: DependencyGraphGenerator
    protected abstract val environment: SymbolProcessorEnvironment
    protected abstract val logger: KSPLogger

    protected open fun verifyOrigin(declaration: KSClassDeclaration) = Unit
    protected open fun findFactory(entryPoint: KSClassDeclaration): GraphEntryPoint.Factory? = null
    protected abstract fun getOrigins(resolver: Resolver): Sequence<KSClassDeclaration>

    final override fun contribute(context: GraphContext) {
        val entryPoints = getOrigins(context.resolver)
        for (entryPoint in entryPoints) {
            verifyOrigin(entryPoint)
            val factory = findFactory(entryPoint)
            val properties = findProperties(entryPoint).toMutableList()
            val virtualFactoryParameters = mutableListOf<Parameter>()
            val methods = findMethods(entryPoint).toMutableList()
            val external = findExternal(context.resolver, properties)
            val type = entryPoint.toType(false)
            val entry = GraphEntryPoint(
                type = type,
                factory = factory ?: type.virtualFactory(virtualFactoryParameters),
                properties = properties,
                methods = methods,
                isModule = entryPoint.isAnnotationPresent(Export::class)
            )
            val graphFactory = DependencyGraph.Factory(
                entry = entry,
                root = context.builder.build(),
                multibinding = context.multibind.build(),
                imports = context.resolver.getDeclarationsFromPackage("spike.generated")
                    .filterIsInstance<KSClassDeclaration>()
                    .map {
                        TypeFactory.Class(it.toType(false), it.primaryConstructor!!.toInvocation(), false, emptyList())
                    },
                logger = logger
            )
            for (e in external) {
                graphFactory.putExternal(e)
            }
            generator.generate(graphFactory.create(), context.originatingFiles) { spec ->
                spec.writeTo(environment.codeGenerator, true)
            }
        }
    }

    private fun findExternal(
        resolver: Resolver,
        properties: MutableList<Member.Property>
    ): Sequence<TypeFactory> {
        return resolver.getDeclarationsFromPackage("spike.generated")
            .filterIsInstance<KSClassDeclaration>()
            .flatMap { klass ->
                sequence {
                    for (f in klass.getDeclaredFunctions()) {
                        val tf = TypeFactory.External(
                            type = f.returnType!!.resolve().toType(), origin = klass.toType(false),
                            name = f.simpleName.asString(),
                            isMethod = true
                        )
                        yield(tf)
                    }
                    for (p in klass.getDeclaredProperties()) {
                        val tf = TypeFactory.External(
                            type = p.type.resolve().toType(), origin = klass.toType(false),
                            name = p.simpleName.asString(),
                            isMethod = false
                        )
                        yield(tf)
                    }
                    for (p in klass.primaryConstructor?.parameters.orEmpty()) {
                        val type = p.type.resolve().toType()
                        properties.add(Member.Property(type.packageName, p.name!!.asString(), type, synthetic = true))
                    }
                }
            }
    }

    private fun findProperties(
        entryPoint: KSClassDeclaration
    ): List<Member.Property> = entryPoint.getAllProperties().filter { it.isAbstract() }.map {
        Member.Property(
            packageName = it.packageName.asString(),
            name = it.simpleName.asString(),
            returns = it.type.resolve().toType().qualifiedBy(it.findQualifiers()),
        )
    }.toList()

    private fun findMethods(
        entryPoint: KSClassDeclaration
    ): List<Member.Method> = entryPoint.getAllFunctions().filter { it.isAbstract }.map {
        check(it.parameters.isEmpty()) {
            """Client error, fix by substituting <actual /> for <expected />; "this" points to the mandatory change:
                |<expected>
                |  @spike.EntryPoint
                |  interface $entryPoint {
                |    val ${it.simpleName.asString()}: ${it.returnType}
                |    @spike.EntryPoint.Factory
                |    interface Factory {
                |       fun create(<parameters... />): $entryPoint
                |    }
                |  }
                |</expected>
                |
                |<actual>
                |  @spike.EntryPoint
                |  interface $entryPoint {
                |    fun ${it.simpleName.asString()}(<parameters... />): ${it.returnType}
                |  }
                |</actual>
                |
                |<description>
                |  Spike accepts input parameters only through EntryPoint.Factory member. Move input parameters of these methods
                |  to the factory member of Factory annotated class
                |</description>
            """.trimMargin()
        }
        Member.Method(
            it.packageName.asString(),
            it.simpleName.asString(),
            it.returnType!!.resolve().toType().qualifiedBy(it.findQualifiers()),
            it.parentDeclaration?.toType(false),
        )
    }.toList()
}
