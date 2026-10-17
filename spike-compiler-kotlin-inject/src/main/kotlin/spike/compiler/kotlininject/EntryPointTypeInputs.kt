package spike.compiler.kotlininject

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import spike.compiler.conversion.EntryPointType
import spike.compiler.conversion.SpikeNames

class EntryPointTypeInputs(
    private val delegate: EntryPointType<KotlinInjectComponent>,
) : EntryPointType<KotlinInjectComponent> {
    override fun of(subject: KotlinInjectComponent): TypeSpec {
        val type = delegate.of(subject)
        val inputs = subject.inputs
        if (inputs.isEmpty()) return type
        val properties = inputs.map { PropertySpec.builder(it.name!!.asString(), it.type.toTypeName()).build() }
        val parameters = inputs.map { ParameterSpec.builder(it.name!!.asString(), it.type.toTypeName()).build() }
        val factory = TypeSpec.interfaceBuilder("Factory")
            .addAnnotation(SpikeNames.EntryPointFactory)
            .addFunction(
                FunSpec.builder("create")
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameters(parameters)
                    .returns(subject.entryPoint)
                    .build()
            )
            .build()
        return type.toBuilder().addProperties(properties).addType(factory).build()
    }
}
