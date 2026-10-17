package spike.compiler.metro

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import spike.compiler.conversion.IncludeFunction
import spike.compiler.conversion.ParameterConversion

/**
 * Implements an @AssistedFactory by forwarding its function to the `(arguments) -> T` function type that Spike
 * builds for assisted injection.
 */
class IncludeFunctionAssistedFactory(
    private val include: ClassName,
    private val parameters: ParameterConversion,
) : IncludeFunction<MetroAssistedFactory> {
    override fun of(subject: MetroAssistedFactory): FunSpec {
        val create = checkNotNull(subject.create) { "Only checked assisted factories are converted" }
        val factoryName = subject.declaration.toClassName()
        val returns = create.returnType!!.toTypeName()
        val specs = create.parameters.map(parameters::parameter)
        val implementation = TypeSpec.anonymousClassBuilder()
            .addSuperinterface(factoryName)
            .addFunction(
                FunSpec.builder(create.simpleName.asString())
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameters(specs)
                    .returns(returns)
                    .addStatement("return build(%L)", specs.map { CodeBlock.of("%N", it) }.joinToCode())
                    .build()
            )
            .build()
        return FunSpec.builder("assisted" + factoryName.simpleNames.joinToString(""))
            .returns(factoryName)
            .addAnnotation(include)
            .addParameter("build", LambdaTypeName.get(parameters = specs, returnType = returns))
            .addStatement("return %L", implementation)
            .build()
    }
}
