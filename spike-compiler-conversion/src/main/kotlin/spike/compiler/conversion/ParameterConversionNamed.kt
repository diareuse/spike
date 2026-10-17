package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec

class ParameterConversionNamed(
    private val names: NameLookup,
    private val named: ClassName,
    private val delegate: ParameterConversion,
) : ParameterConversion by delegate {
    override fun parameter(parameter: KSValueParameter): ParameterSpec {
        val spec = delegate.parameter(parameter)
        val name = names.name(parameter) ?: return spec
        return spec.toBuilder().addAnnotation(AnnotationSpec.builder(named).addMember("%S", name).build()).build()
    }
}
