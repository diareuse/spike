package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter

val KSDeclaration.kind: String
    get() = when (this) {
        is KSFunctionDeclaration -> "function"
        is KSPropertyDeclaration -> "property"
        is KSClassDeclaration -> classKind.type
        else -> "declaration"
    }

val KSDeclaration.described: String
    get() = "$kind ${simpleName.asString()}"

fun KSDeclaration.signature(type: String = declaredType()): String {
    val name = simpleName.asString()
    return when (this) {
        is KSFunctionDeclaration -> "fun $name(${parameters.rendered()}): $type = ..."
        is KSPropertyDeclaration -> "val $name: $type get() = ..."
        else -> "$kind $name"
    }
}

fun List<KSValueParameter>.rendered(): String =
    joinToString { "${it.name?.asString()}: ${it.type.resolve().rendered()}" }

fun KSAnnotation.rendered(): String {
    val arguments = arguments.joinToString { it.value.renderedValue() }
    return "@${shortName.asString()}" + if (arguments.isEmpty()) "" else "($arguments)"
}

private fun KSDeclaration.declaredType(): String = when (this) {
    is KSFunctionDeclaration -> returnType?.resolve()?.rendered().orEmpty()
    is KSPropertyDeclaration -> type.resolve().rendered()
    else -> simpleName.asString()
}

fun KSType.rendered(): String = declaration.simpleName.asString() + if (isMarkedNullable) "?" else ""

private fun Any?.renderedValue(): String = when (this) {
    is String -> "\"$this\""
    is KSType -> declaration.simpleName.asString()
    is KSClassDeclaration -> simpleName.asString()
    else -> toString()
}
