package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import spike.graph.Type

fun Type.toClassName(): ClassName = when (this) {
    is Type.Parametrized -> envelope.toClassName()
    is Type.Qualified -> type.toClassName()
    is Type.Simple -> ClassName(packageName, simpleName)
    is Type.Inner -> ClassName(packageName, names)
}

fun Type.toTypeName(): TypeName = when (this) {
    is Type.Parametrized -> envelope.toClassName().parameterizedBy(typeArguments.map { it.toTypeName() })
    is Type.Qualified -> type.toClassName()
    is Type.Simple -> ClassName(packageName, simpleName)
    is Type.Inner -> ClassName(packageName, names)
}

val Type.descriptor: String
    get() = when (this) {
        is Type.Inner -> parent.descriptor + "__" + simpleName
        is Type.Parametrized -> envelope.descriptor + typeArguments.joinToString("", "_", "_") { it.descriptor }
        is Type.Qualified -> qualifiers.joinToString("") {
            it.type.descriptor + it.arguments.joinToString {
                it.name.replaceFirstChar { it.uppercase() } + it.value.toString().replaceFirstChar { it.uppercase() }
            }
        } + type.descriptor

        is Type.Simple -> simpleName
    }