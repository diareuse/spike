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
