package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType

class AnnotationNames(vararg names: String) {
    private val names = names.toSet()

    val all: Set<String> get() = names

    fun matches(annotation: KSAnnotation) = matches(annotation.annotationType.resolve())

    fun matches(type: KSType) = type.declaration.qualifiedName?.asString() in names

    fun annotates(node: KSAnnotated) = node.annotations.any(::matches)

    fun annotatesAnnotationOf(annotation: KSAnnotation) =
        annotation.annotationType.resolve().declaration.annotations.any(::matches)
}
