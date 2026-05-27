package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.isInternal
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.EntryPoint
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.graph.GraphEntryPoint
import spike.compiler.graph.Member
import spike.compiler.graph.Parameter

@OptIn(KspExperimental::class)
class GraphContributorEntryPoint(
    override val generator: DependencyGraphGenerator,
    override val environment: SymbolProcessorEnvironment,
    override val logger: KSPLogger,
    override val origins: Sequence<KSAnnotated>,
) : GraphContributorOriginator() {

    override fun verifyOrigin(declaration: KSClassDeclaration) {
        verifyInterface(declaration)
        verifyHasCompanion(declaration)
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

    override fun findFactory(entryPoint: KSClassDeclaration): GraphEntryPoint.Factory? {
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

}
