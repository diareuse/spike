package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName

/**
 * Replaces a map key annotation of another framework with the Spike key annotation it maps to in [keys], keyed by
 * the qualified name of the original.
 */
class IncludeFunctionMapKey<in T : Declared>(
    private val keys: Map<String, ClassName>,
    private val delegate: IncludeFunction<T>,
) : IncludeFunction<T> {
    override fun of(subject: T): FunSpec {
        val function = delegate.of(subject)
        val key = subject.declaration.annotations.firstNotNullOfOrNull(::key) ?: return function
        return function.toBuilder().addAnnotation(key).build()
    }

    private fun key(annotation: KSAnnotation): AnnotationSpec? {
        val name = annotation.annotationType.resolve().declaration.qualifiedName?.asString()
        val key = keys[name] ?: return null
        return AnnotationSpec.builder(key).addMember(member(annotation.arguments.single().value)).build()
    }

    private fun member(value: Any?) = when (value) {
        is String -> CodeBlock.of("%S", value)
        is KSType -> CodeBlock.of("%T::class", value.toClassName())
        else -> CodeBlock.of("%L", value)
    }
}
