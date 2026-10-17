package spike.compiler.metro

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import spike.Include
import spike.compiler.conversion.IncludeFunction
import spike.compiler.conversion.SpikeNames

@Include
class IncludeFunctionContribution : IncludeFunction<Contribution> {
    override fun of(subject: Contribution): FunSpec {
        val className = subject.declaration.toClassName()
        return FunSpec.builder("contributed" + className.simpleNames.joinToString(""))
            .returns(subject.supertypes.single().toTypeName())
            .addAnnotation(SpikeNames.Include)
            .addParameter("implementation", className)
            .addStatement("return implementation")
            .build()
    }
}
