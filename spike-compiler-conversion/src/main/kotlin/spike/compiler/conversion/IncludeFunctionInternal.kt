package spike.compiler.conversion

import com.google.devtools.ksp.isInternal
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier

class IncludeFunctionInternal<in T : Declared>(private val delegate: IncludeFunction<T>) : IncludeFunction<T> {
    override fun of(subject: T): FunSpec {
        val function = delegate.of(subject)
        if (!subject.declaration.isInternal()) return function
        return function.toBuilder().addModifiers(KModifier.INTERNAL).build()
    }
}
