@file:OptIn(spike.InternalSpikeApi::class)

package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.isInternal
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.EntryPoint
import spike.compiler.diagnostic.DiagnosticEntryPointCompanion
import spike.compiler.diagnostic.DiagnosticEntryPointFactory
import spike.compiler.diagnostic.DiagnosticEntryPointInterface
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.Involved
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.graph.GraphEntryPoint
import spike.compiler.graph.Member
import spike.compiler.graph.Parameter
import spike.compiler.processor.symbol.SymbolRegistry

@OptIn(KspExperimental::class)
class GraphContributorEntryPoint(
    override val generator: DependencyGraphGenerator,
    override val environment: SymbolProcessorEnvironment,
    override val logger: KSPLogger,
    override val reporter: DiagnosticReporter,
    override val warnings: WarningReporter,
    override val contributions: ContributionImports,
    private val registry: SymbolRegistry,
    override val stores: EntryPointStores = EntryPointStoresLocal,
) : GraphContributorOriginator() {

    override fun getOrigins(resolver: Resolver) = registry.entryPoint(resolver)

    override fun verifyOrigin(declaration: KSClassDeclaration) {
        verifyInterface(declaration)
        verifyHasCompanion(declaration)
    }

    private fun verifyInterface(entryPoint: KSClassDeclaration) {
        if (entryPoint.classKind == ClassKind.INTERFACE) return
        throw DiagnosticException(
            DiagnosticEntryPointInterface(entryPoint.name(), entryPoint.classKind.type, entryPoint.asOrigin())
        )
    }

    private fun verifyHasCompanion(entryPoint: KSClassDeclaration) {
        val companionObjects = entryPoint.declarations
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.isCompanionObject }
            .filter { it.isPublic() || it.isInternal() }

        if (companionObjects.any()) return
        throw DiagnosticException(DiagnosticEntryPointCompanion(entryPoint.name(), entryPoint.asOrigin()))
    }

    override fun findFactory(entryPoint: KSClassDeclaration): GraphEntryPoint.Factory? {
        val factory = entryPoint.declarations
            .filterIsInstance<KSClassDeclaration>()
            .singleOrNull { it.isAnnotationPresent(EntryPoint.Factory::class) }
            ?: return null
        val functions = factory.getDeclaredFunctions().toList()
        val func = functions.singleOrNull() ?: throw DiagnosticException(
            DiagnosticEntryPointFactory(
                factory = "${entryPoint.name()}.${factory.name()}",
                factoryKind = factory.classKind.type,
                factoryOrigin = factory.asOrigin(),
                functions = functions.map { Involved(it.described, it.asOrigin()) },
                entryPoint = entryPoint.name(),
                suggested = "fun create(${functions.firstOrNull()?.parameters?.rendered().orEmpty()}): " +
                    entryPoint.name(),
            )
        )
        val factoryType = factory.toType(false)
        return GraphEntryPoint.Factory(
            origin = func.asOrigin(),
            sharesSingletons = factory.isAnnotationPresent(EntryPoint.Factory.SharesSingletons::class),
            type = factoryType,
            method = Member.Method(
                packageName = func.packageName.asString(),
                name = func.simpleName.asString(),
                returns = func.returnType!!.resolve().toType(),
                parent = factoryType,
                parameters = func.parameters.map {
                    Parameter(
                        name = it.name!!.asString(),
                        type = it.type.toQualifiedType(it),
                        nullable = it.type.resolve().isMarkedNullable,
                    )
                },
            ),
        )
    }


    private fun KSClassDeclaration.name() = simpleName.asString()
}
