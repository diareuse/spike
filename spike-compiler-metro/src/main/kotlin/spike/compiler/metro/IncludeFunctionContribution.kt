package spike.compiler.metro

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import spike.compiler.conversion.IncludeFunction

class IncludeFunctionContribution(private val include: ClassName) : IncludeFunction<Contribution> {
    override fun of(subject: Contribution): FunSpec {
        val className = subject.declaration.toClassName()
        return FunSpec.builder("contributed" + className.simpleNames.joinToString(""))
            .returns(subject.supertypes.single().toTypeName())
            .addAnnotation(include)
            .addParameter("implementation", className)
            .addStatement("return implementation")
            .build()
    }
}
