package spike.compiler.androidx

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import spike.compiler.conversion.AnnotationNames
import spike.compiler.conversion.ParameterConversion

/**
 * Keeps the Spike qualifiers of a parameter, so the generated function asks the graph for the same binding.
 */
class ParameterConversionQualified(
    private val qualifier: AnnotationNames,
    private val delegate: ParameterConversion,
) : ParameterConversion by delegate {
    override fun parameter(parameter: KSValueParameter): ParameterSpec {
        val qualifiers = parameter.annotations.filter(qualifier::annotatesAnnotationOf).map { it.toAnnotationSpec() }
        return delegate.parameter(parameter).toBuilder().addAnnotations(qualifiers.toList()).build()
    }
}
