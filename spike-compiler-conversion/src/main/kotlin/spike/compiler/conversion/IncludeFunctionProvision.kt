package spike.compiler.conversion

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toTypeName

class IncludeFunctionProvision(
    private val include: ClassName,
    private val prefix: String,
    private val parameters: ParameterConversion,
    private val call: ProvisionCall,
) : IncludeFunction<Provision> {
    override fun of(subject: Provision): FunSpec {
        val function = FunSpec.builder(prefix + subject.owner + subject.name.replaceFirstChar(Char::uppercase))
            .returns(subject.type.toTypeName())
            .addAnnotation(include)
        subject.receiver?.let { function.addParameter("receiver", it.toTypeName()) }
        return function
            .addParameters(subject.parameters.map(parameters::parameter))
            .addStatement("%L", call.statement(subject))
            .build()
    }
}
