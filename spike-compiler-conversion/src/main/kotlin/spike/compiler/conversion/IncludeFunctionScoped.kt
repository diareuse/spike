package spike.compiler.conversion

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec

class IncludeFunctionScoped<in T : Declared>(
    private val scopes: ScopeLookup,
    private val singleton: ClassName,
    private val delegate: IncludeFunction<T>,
) : IncludeFunction<T> {
    override fun of(subject: T): FunSpec {
        val function = delegate.of(subject)
        if (!scopes.isScoped(subject.declaration)) return function
        return function.toBuilder().addAnnotation(singleton).build()
    }
}
