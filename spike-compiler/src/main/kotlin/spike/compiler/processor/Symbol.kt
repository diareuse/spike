package spike.compiler.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.*
import spike.graph.Invocation
import spike.graph.Parameter
import spike.graph.Qualifier
import spike.graph.Type

@OptIn(KspExperimental::class)
fun KSAnnotated.findQualifiers() = annotations
    .filter { it.annotationType.resolve().declaration.isAnnotationPresent(spike.Qualifier::class) }
    .map {
        Qualifier(
            it.annotationType.toType(),
            it.arguments.map {
                val value = when (val v = it.value) {
                    is KSType -> v.toType()
                    is KSClassDeclaration -> v.toType()
                    is KSAnnotation -> v.annotationType.toType()
                    else -> v
                }
                Qualifier.Argument(it.name!!.asString(), value)
            }
        )
    }
    .sorted()
    .toList()

@OptIn(KspExperimental::class)
fun KSFunctionDeclaration.toInvocation() = Invocation(
    parameters = this.parameters.map {
        Parameter(
            name = it.name!!.asString(),
            type = it.type.toType(),
            nullable = it.type.resolve().isMarkedNullable
        )
    },
    singleton = (this.parentDeclaration as? KSClassDeclaration)?.run {
        classKind == ClassKind.OBJECT
    } == true
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

fun Type.qualifiedBy(qualifiers: List<Qualifier>): Type {
    if (qualifiers.isEmpty()) return this
    return Type.Qualified(this, qualifiers)
}

fun KSType.toType(): Type {
    if (this.arguments.isEmpty())
        return declaration.toType()
    return Type.Parametrized(
        envelope = declaration.toType(),
        typeArguments = arguments.map { it.type!!.toType() }
    )
}