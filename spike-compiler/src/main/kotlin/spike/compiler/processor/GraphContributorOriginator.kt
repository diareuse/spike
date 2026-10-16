package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.squareup.kotlinpoet.ksp.writeTo
import spike.Export
import spike.compiler.diagnostic.DiagnosticCollector
import spike.compiler.diagnostic.DiagnosticEntryPointMember
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.generator.DependencyGraphGenerator
import spike.compiler.generator.requireKSFile
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.ExportedContribution
import spike.compiler.graph.GraphEntryPoint
import spike.compiler.graph.GraphRole
import spike.compiler.graph.GraphEntryPoint.Companion.virtualFactory
import spike.compiler.graph.Member
import spike.compiler.graph.Parameter
import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory

@OptIn(KspExperimental::class)
abstract class GraphContributorOriginator : GraphContributor {
    protected abstract val generator: DependencyGraphGenerator
    protected abstract val contributions: ContributionImports
    protected abstract val environment: SymbolProcessorEnvironment
    protected abstract val logger: KSPLogger

    protected open fun verifyOrigin(declaration: KSClassDeclaration) = Unit
    protected open fun findFactory(entryPoint: KSClassDeclaration): GraphEntryPoint.Factory? = null
    protected abstract fun getOrigins(resolver: Resolver): Sequence<KSClassDeclaration>

    final override fun contribute(context: GraphContext) {
        val diagnostics = DiagnosticCollector()
        val generated = context.resolver.getDeclarationsFromPackage("spike.generated")
            .filterIsInstance<KSClassDeclaration>()
            .toList()
        for (entryPoint in getOrigins(context.resolver)) {
            diagnostics.attempt { contribute(context, entryPoint, generated) }
        }
        diagnostics.throwCollected()
    }

    private fun contribute(context: GraphContext, entryPoint: KSClassDeclaration, generated: List<KSClassDeclaration>) {
        verifyOrigin(entryPoint)
        val factory = findFactory(entryPoint)
        val properties = findProperties(entryPoint).toMutableList()
        val virtualFactoryParameters = mutableListOf<Parameter>()
        val methods = findMethods(entryPoint).toMutableList()
        val external = findExternal(generated, properties)
        val type = entryPoint.toType(false)
        val entry = GraphEntryPoint(
            type = type,
            factory = factory ?: type.virtualFactory(virtualFactoryParameters),
            properties = properties,
            methods = methods,
            role = if (entryPoint.isAnnotationPresent(Export::class)) GraphRole.Module else GraphRole.App,
            origin = entryPoint.asOrigin(),
        )
        val graphFactory = DependencyGraph.Factory(
            entry = entry,
            root = context.builder.build(),
            multibinding = context.multibind.build(),
            imports = generated.asSequence()
                .map {
                    val invocation = it.primaryConstructor!!.toInvocation()
                    val inputs = invocation.copy(
                        parameters = invocation.parameters.filterNot { p -> p.type == SingletonHolderType }
                    )
                    TypeFactory.Class(it.toType(false), inputs, false, emptyList())
                }
        )
        for ((e, exportedBy) in external) {
            graphFactory.putExternal(e, exportedBy)
        }
        for (contribution in contributions.of(generated)) {
            graphFactory.putContribution(
                contribution.target,
                contribution.key,
                contribution.external,
                contribution.exportedBy,
            )
        }
        generator.generate(graphFactory.create(), context.originatingFiles + entryPoint.requireKSFile()) { spec ->
            spec.writeTo(environment.codeGenerator, true)
        }
    }

    private fun findExternal(
        generated: List<KSClassDeclaration>,
        properties: MutableList<Member.Property>
    ): Sequence<Pair<TypeFactory.External, Type>> {
        return generated.asSequence()
            .flatMap { klass ->
                val exported = klass.superTypes.first().resolve()
                val exportedBy = exported.toType()
                val declaration = exported.declaration as KSClassDeclaration
                val functionQualifiers = declaration.getAllFunctions()
                    .associate { it.name() to it.returnType?.qualifiersWith(it) }
                val propertyQualifiers = declaration.getAllProperties()
                    .associate { it.name() to it.type.qualifiersWith(it) }
                sequence {
                    for (f in klass.getDeclaredFunctions()) {
                        val contribution = ExportedContribution.targetOf(f.name()) != null
                        if (!f.isPublic() || f.isConstructor() || contribution) continue
                        val tf = TypeFactory.External(
                            type = f.returnType!!.resolve().toType()
                                .qualifiedBy(functionQualifiers[f.name()].orEmpty()),
                            origin = klass.toType(false),
                            name = f.simpleName.asString(),
                            isMethod = true
                        )
                        yield(tf to exportedBy)
                    }
                    for (p in klass.getDeclaredProperties()) {
                        if (!p.isPublic()) continue
                        val tf = TypeFactory.External(
                            type = p.type.resolve().toType()
                                .qualifiedBy(propertyQualifiers[p.name()].orEmpty()),
                            origin = klass.toType(false),
                            name = p.simpleName.asString(),
                            isMethod = false
                        )
                        yield(tf to exportedBy)
                    }
                    for (p in klass.primaryConstructor?.parameters.orEmpty()) {
                        val type = p.type.resolve().toType()
                        val imported = properties.any { it.synthetic && it.returns == type }
                        if (type == SingletonHolderType || imported) continue
                        properties.add(Member.Property(type.packageName, p.name!!.asString(), type, synthetic = true))
                    }
                }
            }
    }

    private fun KSDeclaration.name() = simpleName.asString()

    private fun findProperties(
        entryPoint: KSClassDeclaration
    ): List<Member.Property> = entryPoint.getAllProperties().filter { it.isAbstract() }.map {
        Member.Property(
            packageName = it.packageName.asString(),
            name = it.simpleName.asString(),
            returns = it.type.toQualifiedType(it),
        )
    }.toList()

    private fun findMethods(
        entryPoint: KSClassDeclaration
    ): List<Member.Method> = entryPoint.getAllFunctions().filter { it.isAbstract }.map {
        if (it.parameters.isNotEmpty()) {
            throw DiagnosticException(
                DiagnosticEntryPointMember(
                    entryPoint = entryPoint.simpleName.asString(),
                    name = it.simpleName.asString(),
                    returns = it.returnType!!.resolve().rendered(),
                    parameters = it.parameters.rendered(),
                    origin = it.asOrigin(),
                )
            )
        }
        Member.Method(
            it.packageName.asString(),
            it.simpleName.asString(),
            it.returnType!!.toQualifiedType(it),
            it.parentDeclaration?.toType(false),
        )
    }.toList()
}

private val SingletonHolderType = Type.Simple("spike.factory", "SingletonHolder", false)
