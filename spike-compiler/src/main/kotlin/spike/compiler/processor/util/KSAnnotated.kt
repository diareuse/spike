package spike.compiler.processor.util

import com.google.devtools.ksp.symbol.KSAnnotated
import kotlin.reflect.KProperty1

inline fun <reified A, reified P> KSAnnotated.getAnnotationParameter(parameter: KProperty1<A, Any?>): P {
    val annotation = annotations.singleOrNull {
        it.annotationType.resolve().declaration.qualifiedName?.asString() == A::class.qualifiedName
    } ?: error("Annotation ${A::class.qualifiedName} not found in $this")
    val arg = annotation.arguments.singleOrNull { it.name?.asString() == parameter.name }
        ?: error("Annotation parameter ${parameter.name} not found in $this")
    return arg.value as P
}
