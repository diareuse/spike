package spike.compiler.dagger

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import spike.compiler.conversion.Conversion
import spike.compiler.conversion.IncludeFunction

class ModuleConversionFile(
    private val function: IncludeFunction<DaggerBinding>,
    private val codeGenerator: CodeGenerator,
) : Conversion<KSAnnotated> {
    override fun convert(subject: KSAnnotated) {
        val module = checkNotNull(DaggerModule.of(subject)) { "Only checked @Module classes are converted" }
        val name = module.declaration.toClassName()
        val file = FileSpec.builder(name.packageName, "${name.simpleNames.joinToString("_")}_SpikeModule")
        for (binding in module.bindings) file.addFunction(function.of(binding))
        file.build().writeTo(codeGenerator, aggregating = false)
    }
}
