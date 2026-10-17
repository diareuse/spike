package spike.compiler.dagger

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import spike.compiler.conversion.IncludeFunction
import spike.compiler.conversion.ParameterConversion

class IncludeFunctionBinding(
    private val include: ClassName,
    private val parameters: ParameterConversion,
    private val call: BindingCall,
) : IncludeFunction<DaggerBinding> {
    override fun of(subject: DaggerBinding): FunSpec {
        val function = subject.function
        val module = subject.module.toClassName().simpleNames.joinToString("")
        val name = call.prefix(subject) + module + function.simpleName.asString().replaceFirstChar(Char::uppercase)
        val arguments = function.parameters.map(parameters::argument).joinToCode()
        return FunSpec.builder(name)
            .returns(function.returnType!!.toTypeName())
            .addAnnotation(include)
            .addParameters(function.parameters.map(parameters::parameter))
            .addStatement("%L", call.statement(subject, arguments))
            .build()
    }
}
