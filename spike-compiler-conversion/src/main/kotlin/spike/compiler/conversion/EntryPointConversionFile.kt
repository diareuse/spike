package spike.compiler.conversion

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo

class EntryPointConversionFile<T : EntryPointSubject>(
    private val read: (KSAnnotated) -> T?,
    private val type: EntryPointType<T>,
    private val codeGenerator: CodeGenerator,
) : Conversion<KSAnnotated> {
    override fun convert(subject: KSAnnotated) {
        val component = checkNotNull(read(subject)) { "Only checked components are converted" }
        FileSpec.builder(component.entryPoint)
            .addType(type.of(component))
            .build()
            .writeTo(codeGenerator, aggregating = false)
    }
}
