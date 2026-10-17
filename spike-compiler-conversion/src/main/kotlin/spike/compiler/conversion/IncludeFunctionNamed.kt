package spike.compiler.conversion

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec

class IncludeFunctionNamed<in T : Declared>(
    private val names: NameLookup,
    private val named: ClassName,
    private val delegate: IncludeFunction<T>,
) : IncludeFunction<T> {
    override fun of(subject: T): FunSpec {
        val function = delegate.of(subject)
        val name = names.name(subject.declaration) ?: return function
        return function.toBuilder().addAnnotation(AnnotationSpec.builder(named).addMember("%S", name).build()).build()
    }
}
