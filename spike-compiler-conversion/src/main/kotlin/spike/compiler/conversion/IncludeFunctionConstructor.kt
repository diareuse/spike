package spike.compiler.conversion

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.toClassName

class IncludeFunctionConstructor(
    private val include: ClassName,
    private val parameters: ParameterConversion,
) : IncludeFunction<Injection> {
    override fun of(subject: Injection): FunSpec {
        val className = subject.owner.toClassName()
        val arguments = subject.constructor.parameters.map(parameters::argument).joinToCode()
        return FunSpec.builder("inject" + className.simpleNames.joinToString("_"))
            .returns(className)
            .addAnnotation(include)
            .addParameters(subject.constructor.parameters.map(parameters::parameter))
            .addStatement("return %T(%L)", className, arguments)
            .build()
    }
}
