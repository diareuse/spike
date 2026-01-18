package spike.compiler.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.EntryPoint
import spike.compiler.generator.DependencyGraphGenerator
import spike.graph.GraphEntryPoint
import spike.graph.Member
import spike.graph.Parameter

@OptIn(KspExperimental::class)
class GraphContributorEntryPoint(
    private val generator: DependencyGraphGenerator
) : GraphContributor {
    override fun contribute(context: GraphContext, resolver: Resolver) {
        val entryPoints = resolver
            .getSymbolsWithAnnotation(EntryPoint::class.qualifiedName!!)
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
                methods = methods
            )
            val graph = context.builder.build(entry)
            generator.generate(graph)
        }
    }

    private fun verifyInterface(entryPoint: KSClassDeclaration) {
        check(entryPoint.classKind == ClassKind.INTERFACE) {
            "Entry point must be an interface"
        }
    }

    private fun verifyHasCompanion(entryPoint: KSClassDeclaration) {
        val companionObjects = entryPoint.declarations
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.isCompanionObject }
            .filter { it.isPublic() || it.isInternal() }

        check(companionObjects.any()) {
            "Entry point must have a public (or internal) companion object"
        }
    }

    private fun findFactory(entryPoint: KSClassDeclaration): GraphEntryPoint.Factory? {
        val factory = entryPoint.declarations
            .filterIsInstance<KSClassDeclaration>()
            .singleOrNull { it.isAnnotationPresent(EntryPoint.Factory::class) }
            ?: return null
        val func = checkNotNull(factory.getDeclaredFunctions().singleOrNull()) {
            "Entry point factory must have a single abstract function"
        }
        val factoryType = factory.toType()
        return GraphEntryPoint.Factory(
            type = factoryType,
            method = Member.Method(
                packageName = func.packageName.asString(),
                name = func.simpleName.asString(),
                returns = func.returnType!!.toType(),
                parent = factoryType,
                parameters = func.parameters.map {
                    Parameter(
                        name = it.name!!.asString(),
                        type = it.type.toType().qualifiedBy(it.findQualifiers()),
                        nullable = it.type.resolve().isMarkedNullable
                    )
                }
            )
        )
    }

    private fun findProperties(entryPoint: KSClassDeclaration): List<Member.Property> {
        return entryPoint.getAllProperties().filter { it.isAbstract() }.map {
            Member.Property(
                packageName = it.packageName.asString(),
                name = it.simpleName.asString(),
                returns = it.type.toType().qualifiedBy(it.findQualifiers())
            )
        }.toList()
    }

    private fun findMethods(entryPoint: KSClassDeclaration): List<Member.Method> {
        return entryPoint.getAllFunctions().filter { it.isAbstract }.map {
            check(it.parameters.isEmpty()) {
                "Entry point methods must not have parameters, prefer properties for concise syntax. Found ${it.simpleName.asString()} in ${it.parentDeclaration?.qualifiedName?.asString()} (${it.parameters.joinToString { "${it.name?.asString()}: ${it.type.resolve().declaration.qualifiedName?.asString()}" }})"
            }
            Member.Method(
                it.packageName.asString(),
                it.simpleName.asString(),
                it.returnType!!.toType().qualifiedBy(it.findQualifiers()),
                it.parentDeclaration?.toType()
            )
        }.toList()
    }
}