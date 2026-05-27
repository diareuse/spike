package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.writeTo
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.GraphEntryPoint
import spike.compiler.graph.Member
import spike.compiler.graph.TypeFactory

@OptIn(KspExperimental::class)
abstract class GraphContributorOriginator : GraphContributor {
    protected abstract val generator: DependencyGraphGenerator
    protected abstract val environment: SymbolProcessorEnvironment
    protected abstract val logger: KSPLogger
    protected abstract val origins: Sequence<KSAnnotated>

    protected open fun verifyOrigin(declaration: KSClassDeclaration) = Unit
    protected open fun findFactory(entryPoint: KSClassDeclaration): GraphEntryPoint.Factory? = null

    final override fun contribute(context: GraphContext, resolver: Resolver) {
        val entryPoints = origins.filterIsInstance<KSClassDeclaration>()
        for (entryPoint in entryPoints) {
            verifyOrigin(entryPoint)
            val factory = findFactory(entryPoint)
            val properties = findProperties(entryPoint)
            val methods = findMethods(entryPoint)
            val entry = GraphEntryPoint.Companion(
                type = entryPoint.toType(),
                factory = factory,
                properties = properties,
                methods = methods,
            )
            val graphFactory = DependencyGraph.Factory(
                entry = entry,
                root = context.builder.build(),
                multibinding = context.multibind.build(),
                logger = logger
            )
            for (e in findExternal(context, resolver)) {
                graphFactory.putExternal(e)
            }
            generator.generate(graphFactory.create(), context.originatingFiles) { spec ->
                try {
                    spec.writeTo(environment.codeGenerator, true)
                } catch (e: FileAlreadyExistsException) {
                    spec.writeTo(e.file.parentFile)
                }
            }
        }
    }

    private fun findExternal(context: GraphContext, resolver: Resolver): Sequence<TypeFactory.External> {
        return resolver.getDeclarationsFromPackage("spike.generated")
            //.onEach { context.originatingFiles += it.containingFile ?: it.requireKSFile() }
            .filterIsInstance<KSClassDeclaration>()
            .flatMap { klass ->
                sequence {
                    for (f in klass.getDeclaredFunctions()) {
                        val tf = TypeFactory.External(
                            type = f.returnType!!.resolve().toType(), origin = klass.toType(),
                            name = f.simpleName.asString(),
                            isMethod = true
                        )
                        yield(tf)
                    }
                    for (p in klass.getDeclaredProperties()) {
                        val tf = TypeFactory.External(
                            type = p.type.resolve().toType(), origin = klass.toType(),
                            name = p.simpleName.asString(),
                            isMethod = false
                        )
                        yield(tf)
                    }
                }
            }
    }

    private fun findProperties(entryPoint: KSClassDeclaration): List<Member.Property> = entryPoint.getAllProperties().filter { it.isAbstract() }.map {
        Member.Property(
            packageName = it.packageName.asString(),
            name = it.simpleName.asString(),
            returns = it.type.resolve().toType().qualifiedBy(it.findQualifiers()),
        )
    }.toList()

    private fun findMethods(entryPoint: KSClassDeclaration): List<Member.Method> = entryPoint.getAllFunctions().filter { it.isAbstract }.map {
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
            it.parentDeclaration?.toType(),
        )
    }.toList()
}