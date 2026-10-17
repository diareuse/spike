package spike.compiler.androidx

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.toClassName
import spike.compiler.conversion.IncludeFunction
import spike.compiler.conversion.ParameterConversion
import spike.compiler.conversion.SpikeNames

class IncludeFunctionViewModel(private val parameters: ParameterConversion) : IncludeFunction<ViewModelInclusion> {
    override fun of(subject: ViewModelInclusion): FunSpec {
        val className = subject.declaration.toClassName()
        val arguments = subject.constructor.parameters.map(parameters::argument).joinToCode()
        return FunSpec.builder("spikeViewModel" + className.simpleNames.joinToString(""))
            .returns(ViewModelNames.ViewModel)
            .addAnnotation(
                AnnotationSpec.builder(SpikeNames.Include).addMember("bindTo = %T.Map", SpikeNames.BindTarget).build(),
            )
            .addAnnotation(
                AnnotationSpec.builder(ViewModelNames.ViewModelKey).addMember("%T::class", className).build(),
            )
            .addParameters(subject.constructor.parameters.map(parameters::parameter))
            .addStatement("return %T(%L)", className, arguments)
            .build()
    }
}
