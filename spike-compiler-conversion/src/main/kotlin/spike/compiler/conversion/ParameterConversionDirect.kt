package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ksp.toTypeName

class ParameterConversionDirect : ParameterConversion {
    override fun parameter(parameter: KSValueParameter) =
        ParameterSpec.builder(parameter.name!!.asString(), parameter.type.resolve().toTypeName()).build()

    override fun argument(parameter: KSValueParameter) = CodeBlock.of("%L", parameter.name!!.asString())
}
