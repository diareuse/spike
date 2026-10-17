package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

/**
 * Takes a [replacement] from the Spike graph where the converted declaration takes one of [wrappers], and adapts it
 * back by reading [accessor] inside a SAM conversion to the wrapper.
 */
class ParameterConversionWrapped(
    private val wrappers: AnnotationNames,
    private val replacement: ClassName,
    private val accessor: String,
    private val fallback: ParameterConversion,
) : ParameterConversion {
    override fun parameter(parameter: KSValueParameter): ParameterSpec {
        val type = parameter.type.resolve()
        if (!wrappers.matches(type)) return fallback.parameter(parameter)
        val typeName = replacement.parameterizedBy(type.arguments.single().toTypeName())
        return ParameterSpec.builder(parameter.name!!.asString(), typeName).build()
    }

    override fun argument(parameter: KSValueParameter): CodeBlock {
        val type = parameter.type.resolve()
        if (!wrappers.matches(type)) return fallback.argument(parameter)
        val wrapper = (type.declaration as KSClassDeclaration).toClassName()
        return CodeBlock.of("%T { %L.%L }", wrapper, parameter.name!!.asString(), accessor)
    }
}
