package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.isInternal
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.writeTo
import spike.EntryPoint
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.GraphEntryPoint
import spike.compiler.graph.Member
import spike.compiler.graph.Parameter

@OptIn(KspExperimental::class)
class GraphContributorEntryPoint(
    private val generator: DependencyGraphGenerator,
    private val environment: SymbolProcessorEnvironment,
    private val logger: KSPLogger,
    private val selector: (Resolver) -> Sequence<KSAnnotated>,
) : GraphContributor {
    override fun contribute(context: GraphContext, resolver: Resolver) {
        val entryPoints = selector(resolver)
            .filterIsInstance<KSClassDeclaration>()
        for (entryPoint in entryPoints) {
            verifyInterface(entryPoint)
            verifyHasCompanion(entryPoint)
            val factory = findFactory(entryPoint)
            val properties = findProperties(entryPoint)
            val methods = findMethods(entryPoint)
            val entry = GraphEntryPoint(
                type = entryPoint.toType(),
                factory = factory,
                properties = properties,
                methods = methods,
            )
            val graph = DependencyGraph.Factory(
                entry = entry,
                root = context.builder.build(),
                multibinding = context.multibind.build(),
                logger = logger
            ).create()
            generator.generate(graph, context.originatingFiles) { spec ->
                try {
                    spec.writeTo(environment.codeGenerator, true)
                } catch (e: FileAlreadyExistsException) {
                    spec.writeTo(e.file.parentFile)
                }
            }
        }
    }

    private fun verifyInterface(entryPoint: KSClassDeclaration) {
        check(entryPoint.classKind == ClassKind.INTERFACE) {
            """Client error, fix by substituting <actual /> for <expected />; "this" points to the mandatory change:
                |<expected>
                |  @spike.EntryPoint
                |  interface /* <- this */ $entryPoint { /**/ }
                |</expected>
                |
                |<actual>
                |  @spike.EntryPoint
                |  ${entryPoint.classKind.type} $entryPoint { /**/ }
                |</actual>
                |
                |<description>
                |  Interfaces are enforced to allow future features, expandability and inheritance.
                |</description>
            """.trimMargin()
        }
    }

    private fun verifyHasCompanion(entryPoint: KSClassDeclaration) {
        val companionObjects = entryPoint.declarations
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.isCompanionObject }
            .filter { it.isPublic() || it.isInternal() }

        check(companionObjects.any()) {
            """Client error, fix by substituting <actual /> for <expected />; "this" points to the mandatory change:
                |<expected>
                |  @spike.EntryPoint
                |  interface $entryPoint {
                |    // ...
                |    companion object // <- this
                |  }
                |</expected>
                |
                |<actual>
                |  @spike.EntryPoint
                |  interface $entryPoint {
                |    // ...
                |  }
                |</actual>
                |
                |<description>
                |  Companion objects are used as a receiver for factories and implicit builders. Spike doesn't believe in generating uncoupled classes to your original code.
                |</description>
            """.trimMargin()
        }
    }

    private fun findFactory(entryPoint: KSClassDeclaration): GraphEntryPoint.Factory? {
        val factory = entryPoint.declarations
            .filterIsInstance<KSClassDeclaration>()
            .singleOrNull { it.isAnnotationPresent(EntryPoint.Factory::class) }
            ?: return null
        val func = checkNotNull(factory.getDeclaredFunctions().singleOrNull()) {
            """Client error, fix by substituting <actual /> for <expected />; "this" points to the mandatory change:
                |<expected>
                |  @spike.EntryPoint
                |  interface $entryPoint {
                |    @spike.EntryPoint.Factory
                |    interface $factory {
                |      fun create(/*params*/): $entryPoint // <- this
                |    }
                |  }
                |</expected>
                |
                |<actual>
                |  @spike.EntryPoint
                |  interface $entryPoint {
                |    @spike.EntryPoint.Factory
                |    interface $factory {
                |      // zero methods or many methods
                |    }
                |  }
                |</actual>
                |
                |<description>
                |  Spike uses factory parameters as static inputs to your dependency graph. You must declare them statically.
                |  If some of your parameters are optional, declare them as nullable and pass null.
                |</description>
            """.trimMargin()
        }
        val factoryType = factory.toType()
        return GraphEntryPoint.Factory(
            type = factoryType,
            method = Member.Method(
                packageName = func.packageName.asString(),
                name = func.simpleName.asString(),
                returns = func.returnType!!.resolve().toType(),
                parent = factoryType,
                parameters = func.parameters.map {
                    Parameter(
                        name = it.name!!.asString(),
                        type = it.type.resolve().toType().qualifiedBy(it.findQualifiers()),
                        nullable = it.type.resolve().isMarkedNullable,
                    )
                },
            ),
        )
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
