package spike.compiler.conversion

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo

class IncludeConversionFile<T : Included>(
    private val read: (KSAnnotated) -> T?,
    private val function: IncludeFunction<T>,
    private val codeGenerator: CodeGenerator,
) : Conversion<KSAnnotated> {
    override fun convert(subject: KSAnnotated) {
        val included = checkNotNull(read(subject)) { "Only checked declarations are converted" }
        FileSpec.builder(included.packageName, included.fileName)
            .addFunction(function.of(included))
            .build()
            .writeTo(codeGenerator, aggregating = false)
    }
}
