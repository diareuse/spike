package spike.compiler.metro

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.conversion.EntryPointType
import spike.compiler.conversion.ParameterConversion
import spike.compiler.conversion.SpikeNames

class EntryPointTypeGraphFactory(
    private val parameters: ParameterConversion,
    private val delegate: EntryPointType<MetroGraph>,
) : EntryPointType<MetroGraph> {
    override fun of(subject: MetroGraph): TypeSpec {
        val type = delegate.of(subject)
        val create = subject.factory ?: return type
        val factory = TypeSpec.interfaceBuilder("Factory")
            .addAnnotation(SpikeNames.EntryPointFactory)
            .addFunction(
                FunSpec.builder(create.simpleName.asString())
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameters(create.parameters.map(parameters::parameter))
                    .returns(subject.entryPoint)
                    .build()
            )
            .build()
        return type.toBuilder().addType(factory).build()
    }
}
