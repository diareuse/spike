package spike.compiler.processor.util

import com.google.devtools.ksp.symbol.KSAnnotated
import kotlin.reflect.KProperty1

inline fun <reified A, reified P> KSAnnotated.getAnnotationParameterOrDefault(parameter: KProperty1<A, Any?>, default: P): P {
    val annotation = annotations.singleOrNull {
        it.annotationType.resolve().declaration.qualifiedName?.asString() == A::class.qualifiedName
    } ?: return default
    val arg = annotation.arguments.singleOrNull { it.name?.asString() == parameter.name } ?: return default
    return arg.value as P
}
