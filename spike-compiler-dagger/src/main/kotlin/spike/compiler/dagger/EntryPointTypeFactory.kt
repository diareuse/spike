package spike.compiler.dagger

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import spike.compiler.conversion.EntryPointType
import spike.compiler.conversion.ParameterConversion
import spike.compiler.conversion.SpikeNames
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.Order
import spike.compiler.conversion.EntryPointTypeDecorator

class EntryPointTypeFactory(
    private val parameters: ParameterConversion,
    private val delegate: EntryPointType<DaggerComponent>,
) : EntryPointType<DaggerComponent> {
    override fun of(subject: DaggerComponent): TypeSpec {
        val type = delegate.of(subject)
        val factory = subject.factory ?: return type
        return type.toBuilder().addType(factory(subject.entryPoint, factory)).build()
    }

    private fun factory(entryPoint: ClassName, factory: KSClassDeclaration): TypeSpec {
        val create = factory.getDeclaredFunctions().single()
        return TypeSpec.interfaceBuilder("Factory")
            .addSuperinterface(factory.toClassName())
            .addAnnotation(SpikeNames.EntryPointFactory)
            .addFunction(
                FunSpec.builder(create.simpleName.asString())
                    .addModifiers(KModifier.ABSTRACT, KModifier.OVERRIDE)
                    .addParameters(create.parameters.map(parameters::parameter))
                    .returns(entryPoint)
                    .build()
            )
            .build()
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(20)
        fun decorator(parameters: ParameterConversion): EntryPointTypeDecorator<DaggerComponent> =
            EntryPointTypeDecorator { EntryPointTypeFactory(parameters, it) }
    }
}
