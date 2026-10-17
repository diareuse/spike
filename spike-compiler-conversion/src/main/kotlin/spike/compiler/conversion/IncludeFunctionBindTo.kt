package spike.compiler.conversion

import com.squareup.kotlinpoet.FunSpec

/**
 * Makes the @Include function of a declaration annotated with one of [contributions] contribute to the [target]
 * multibinding instead of binding its type.
 */
class IncludeFunctionBindTo<in T : Declared>(
    private val contributions: AnnotationNames,
    private val target: String,
    private val delegate: IncludeFunction<T>,
) : IncludeFunction<T> {
    override fun of(subject: T): FunSpec {
        val function = delegate.of(subject)
        if (!contributions.annotates(subject.declaration)) return function
        val builder = function.toBuilder()
        builder.annotations.replaceAll { annotation ->
            if (annotation.typeName != SpikeNames.Include) return@replaceAll annotation
            annotation.toBuilder().addMember("bindTo = %T.%L", SpikeNames.BindTarget, target).build()
        }
        return builder.build()
    }
}
