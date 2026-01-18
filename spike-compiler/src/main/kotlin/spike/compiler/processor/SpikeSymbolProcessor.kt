package spike.compiler.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.ksp.writeTo
import spike.EntryPoint
import spike.Include
import spike.Inject
import spike.Singleton
import spike.compiler.generator.*
import spike.compiler.generator.container.DependencyContainerFileChain
import spike.compiler.generator.container.DependencyContainerFileWithType
import spike.compiler.generator.container.DependencyContainerTypeChain
import spike.compiler.generator.container.DependencyContainerTypeConstructor
import spike.compiler.generator.container.DependencyContainerTypeFactory
import spike.compiler.generator.container.DependencyContainerTypeInternal
import spike.compiler.generator.entrypoint.EntryPointFileChain
import spike.compiler.generator.entrypoint.EntryPointFileInitializer
import spike.compiler.generator.entrypoint.EntryPointFileWithType
import spike.compiler.generator.entrypoint.EntryPointTypeChain
import spike.compiler.generator.entrypoint.EntryPointTypeConstructor
import spike.compiler.generator.entrypoint.EntryPointTypeFunctions
import spike.compiler.generator.entrypoint.EntryPointTypeInternal
import spike.compiler.generator.entrypoint.EntryPointTypeProperties
import spike.compiler.generator.entrypoint.EntryPointTypeSuperinterface
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryFileChain
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryFileWithType
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeChain
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeInternal
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeMethod
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeSuperinterface
import spike.graph.*
import kotlin.reflect.KClass

@OptIn(KspExperimental::class)
class SpikeSymbolProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val builder = DependencyGraph.Builder(environment.logger::warn)

        resolver.getSymbolsWithAnnotation(Include::class.qualifiedName!!).forEach {
            val bindAs = it.getKSAnnotationByType(Include::class)
                .single().arguments.first { it.name?.asString() == Include::bindAs.name }.value as KSType
            when (it) {
                is KSClassDeclaration -> {
                    check(bindAs.isAssignableFrom(it.asStarProjectedType())) {
                        "Bind target (${it.qualifiedName?.asString()}) must be assignable from the '${Include::bindAs.name}' class (${bindAs.declaration.qualifiedName?.asString()})"
                    }
                    if (bindAs.toType() != AnyType) {
                        builder.addBinder(
                            from = it.toType().qualifiedBy(it.findQualifiers()),
                            to = bindAs.toType().qualifiedBy(it.findQualifiers())
                        )
                    }
                    val constructors = it.getConstructors().toList()
                    val constructor = when {
                        constructors.size > 1 -> checkNotNull(constructors.firstOrNull { it.isAnnotationPresent(Inject::class) }) {
                            "Include class must have a constructor annotated with @spike.Inject if it has more than one constructor"
                        }

                        else -> constructors.single()
                    }
                    builder.addConstructor(
                        type = it.toType().qualifiedBy(it.findQualifiers()),
                        invocation = constructor.toInvocation(),
                        singleton = it.isAnnotationPresent(Singleton::class)
                    )
                }

                is KSFunctionDeclaration -> {
                    check(bindAs.isAssignableFrom(it.returnType!!.resolve())) {
                        "Bind target (${it.qualifiedName?.asString()}:${it.returnType?.toType()}) must be assignable from the '${Include::bindAs.name}' function (${bindAs.declaration.qualifiedName?.asString()})"
                    }
                    if (bindAs.toType() != AnyType) {
                        builder.addBinder(
                            from = it.returnType!!.toType().qualifiedBy(it.findQualifiers()),
                            to = bindAs.toType().qualifiedBy(it.findQualifiers())
                        )
                    }
                    builder.addFactory(
                        type = it.returnType!!.toType().qualifiedBy(it.findQualifiers()),
                        member = Member.Method(
                            packageName = it.packageName.asString(),
                            name = it.simpleName.asString(),
                            returns = it.returnType!!.toType().qualifiedBy(it.findQualifiers()),
                            parent = it.parentDeclaration?.toType()?.qualifiedBy(it.findQualifiers())
                        ),
                        invocation = it.toInvocation(),
                        singleton = it.isAnnotationPresent(Singleton::class)
                    )
                }

                else -> error("Include annotation can only be used on classes and functions")
            }
        }

        resolver
            .getSymbolsWithAnnotation(EntryPoint::class.qualifiedName!!)
            .map {
                check(it is KSClassDeclaration && it.classKind == ClassKind.INTERFACE) {
                    "Entry point must be an interface"
                }
                check(it.declarations.any { it is KSClassDeclaration && it.isCompanionObject }) {
                    "Entry point must have a companion object"
                }
                val factoryClass = resolver.getSymbolsWithAnnotation(EntryPoint.Factory::class.qualifiedName!!)
                    .filterIsInstance<KSClassDeclaration>()
                    .filter { it.classKind == ClassKind.INTERFACE }
                    .singleOrNull { inner ->
                        inner.getDeclaredFunctions().singleOrNull()?.returnType?.toType() == it.toType()
                    }
                val factory = if (factoryClass != null) {
                    val func = checkNotNull(factoryClass.getDeclaredFunctions().singleOrNull()) {
                        "Entry point factory must have a single abstract function"
                    }
                    val params = func.parameters.map {
                        Parameter(
                            it.name!!.asString(),
                            it.type.toType().qualifiedBy(it.findQualifiers()),
                            it.type.resolve().isMarkedNullable
                        )
                    }
                    GraphEntryPoint.Factory(
                        type = factoryClass.toType(),
                        method = Member.Method(
                            packageName = factoryClass.packageName.asString(),
                            name = func.simpleName.asString(),
                            returns = func.returnType!!.toType(),
                            parent = factoryClass.toType(),
                            parameters = params
                        )
                    )
                } else null
                GraphEntryPoint(
                    type = it.toType(),
                    factory = factory,
                    properties = it.getAllProperties().filter { it.isAbstract() }.map {
                        Member.Property(
                            packageName = it.packageName.asString(),
                            name = it.simpleName.asString(),
                            returns = it.type.toType().qualifiedBy(it.findQualifiers())
                        )
                    }.toList(),
                    methods = it.getAllFunctions().filter { it.isAbstract }.map {
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
                )
            }
            .forEach {
                builder.build(it).materialize()
            }
        return emptyList()
    }

    fun KSAnnotated.getKSAnnotationByType(annotation: KClass<out Annotation>): Sequence<KSAnnotation> {
        return annotations.filter { it.annotationType.resolve().declaration.qualifiedName?.asString() == annotation.qualifiedName }
    }

    fun DependencyGraph.materialize() {
        val resolver = TypeResolver()
        val dependencyContainerType = DependencyContainerTypeChain(
            subject = this,
            generators = listOf(
                DependencyContainerTypeConstructor(),
                DependencyContainerTypeFactory(),
                DependencyContainerTypeInternal()
            ),
            resolver = resolver
        )
        val dependencyContainerFile = DependencyContainerFileChain(
            subject = this,
            generators = listOf(
                DependencyContainerFileWithType(dependencyContainerType)
            ),
            resolver = resolver
        )
        dependencyContainerFile.proceed().build().writeTo(environment.codeGenerator, false)

        val entryPointFactoryType = EntryPointFactoryTypeChain(
            subject = entry.factory,
            generators = listOf(
                EntryPointFactoryTypeSuperinterface(),
                EntryPointFactoryTypeInternal(),
                EntryPointFactoryTypeMethod()
            ),
            resolver = resolver
        )
        val entryPointFactoryFile = EntryPointFactoryFileChain(
            subject = entry.factory,
            generators = listOf(
                EntryPointFactoryFileWithType(entryPointFactoryType)
            ),
            resolver = resolver
        )
        entryPointFactoryFile.proceed().build().writeTo(environment.codeGenerator, false)

        val entryPointType = EntryPointTypeChain(
            subject = entry,
            generators = listOf(
                EntryPointTypeInternal(),
                EntryPointTypeSuperinterface(),
                EntryPointTypeConstructor(),
                EntryPointTypeProperties(),
                EntryPointTypeFunctions()
            ),
            resolver = resolver
        )
        val entryPointFile = EntryPointFileChain(
            subject = entry,
            generators = listOf(
                EntryPointFileWithType(entryPointType),
                EntryPointFileInitializer()
            ),
            resolver = resolver
        )
        entryPointFile.proceed().build().writeTo(environment.codeGenerator, false)
    }

}
