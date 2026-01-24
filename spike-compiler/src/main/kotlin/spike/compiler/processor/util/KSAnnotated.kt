package spike.compiler.processor.util

import com.google.devtools.ksp.symbol.KSAnnotated
import kotlin.reflect.KProperty1

inline fun <reified A, reified P> KSAnnotated.getAnnotationParameter(parameter: KProperty1<A, Any?>): P {
    val annotation = annotations.single {
        it.annotationType.resolve().declaration.qualifiedName?.asString() == A::class.qualifiedName
    }
    return annotation.arguments.single { it.name?.asString() == parameter.name }.value as P
}