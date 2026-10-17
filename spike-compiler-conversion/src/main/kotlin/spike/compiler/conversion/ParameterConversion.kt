package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterSpec

/**
 * Turns a parameter of a converted declaration into a parameter of the generated @Include function, and into the
 * argument that passes it on to the converted declaration.
 */
interface ParameterConversion {
    fun parameter(parameter: KSValueParameter): ParameterSpec
    fun argument(parameter: KSValueParameter): CodeBlock
}
