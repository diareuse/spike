package spike.compiler.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.*
import spike.*
import spike.compiler.generator.generateEntryPoint
import spike.graph.*
import kotlin.reflect.KClass

@OptIn(KspExperimental::class)
class SpikeSymbolProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val builder = DependencyGraph.Builder(environment.logger::warn)

        resolver.getSymbolsWithAnnotation(Include::class.qualifiedName!!).forEach {
            it.getAnnotationsByType(Include::class).single()
            val bindAs = it.getKSAnnotationByType(Include::class).single().arguments.first { it.name?.asString() == Include::bindAs.name }.value as KSType
            when (it) {
                is KSClassDeclaration -> {
                    check(bindAs.isAssignableFrom(it.asStarProjectedType())) {
                        "Bind target (${it.qualifiedName?.asString()}) must be assignable from the '${Include::bindAs.name}' class (${bindAs.declaration.qualifiedName?.asString()})"
                    }
                    if(bindAs.toType() != AnyType) {
                        builder.addBinder(it.toType(), bindAs.toType())
                    }
                    val constructors = it.getConstructors().toList()
                    val constructor = when {
                        constructors.size > 1 -> checkNotNull(constructors.firstOrNull { it.isAnnotationPresent(Inject::class) }) {
                            "Include class must have a constructor annotated with @spike.Inject if it has more than one constructor"
                        }

                        else -> constructors.single()
                    }
                    builder.addConstructor(
                        type = it.toType(),
                        invocation = constructor.toInvocation(),
                        singleton = it.isAnnotationPresent(Singleton::class)
                    )
                }

                is KSFunctionDeclaration -> {
                    check(bindAs.isAssignableFrom(it.returnType!!.resolve())) {
                        "Bind target (${it.qualifiedName?.asString()}:${it.returnType?.toType()}) must be assignable from the '${Include::bindAs.name}' function (${bindAs.declaration.qualifiedName?.asString()})"
                    }
                    if(bindAs.toType() != AnyType) {
                        builder.addBinder(it.toType(), bindAs.toType())
                    }
                    builder.addFactory(
                        type = it.returnType!!.toType(),
                        member = Member.Method(
                            packageName = it.packageName.asString(),
                            name = it.simpleName.asString(),
                            returns = it.returnType!!.toType(),
                            parent = it.parentDeclaration?.toType()
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
                        Parameter(it.name!!.asString(), it.type.toType(), it.type.resolve().isMarkedNullable)
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
                            returns = it.type.toType()
                        )
                    }.toList(),
                    methods = it.getAllFunctions().filter { it.isAbstract }.map {
                        check(it.parameters.isEmpty()) {
                            "Entry point methods must not have parameters, prefer properties for concise syntax. Found ${it.simpleName.asString()} in ${it.parentDeclaration?.qualifiedName?.asString()} (${it.parameters.joinToString { "${it.name?.asString()}: ${it.type.resolve().declaration.qualifiedName?.asString()}" }})"
                        }
                        Member.Method(
                            it.packageName.asString(),
                            it.simpleName.asString(),
                            it.returnType!!.toType(),
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
        environment.generateEntryPoint(this)
    }

}

@OptIn(KspExperimental::class)
fun KSFunctionDeclaration.toInvocation() = Invocation(
    parameters = this.parameters.map {
        Parameter(
            name = it.name!!.asString(),
            type = it.type.toType(),
            nullable = it.type.resolve().isMarkedNullable
        )
    },
    singleton = (this.parentDeclaration as KSClassDeclaration).run {
        classKind == ClassKind.OBJECT
    }
)

fun KSDeclaration.toType(): Type {
    val pd = parentDeclaration
    if (pd != null) return Type.Inner(pd.toType(), simpleName.asString())
    return Type.Simple(
        packageName = packageName.asString(),
        simpleName = simpleName.asString()
    )
}

fun KSTypeReference.toType(): Type {
    return resolve().toType()
}

fun KSType.toType(): Type {
    if (this.arguments.isEmpty())
        return declaration.toType()
    return Type.Parametrized(
        envelope = declaration.toType(),
        typeArguments = arguments.map { it.type!!.toType() }
    )
}