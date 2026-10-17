package spike.compiler.koin

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import spike.compiler.conversion.Conversion
import spike.Include

/**
 * Writes `fun EntryPoint.koinModule(): Module`, a Koin module with a factory for each exported member.
 */
@Include
class KoinModuleConversionFile(
    private val members: ExportedMembers,
    private val codeGenerator: CodeGenerator,
) : Conversion<KSAnnotated> {
    override fun convert(subject: KSAnnotated) {
        val entryPoint = subject as KSClassDeclaration
        val name = entryPoint.toClassName()
        val body = CodeBlock.builder().beginControlFlow("return %M", KoinNames.Module)
        for (member in members.of(entryPoint)) {
            body.addStatement("factory<%T> { this@koinModule.%L }", member.type, member.access)
        }
        val function = FunSpec.builder("koinModule")
            .receiver(name)
            .returns(KoinNames.ModuleType)
            .addCode(body.endControlFlow().build())
        entryPoint.containingFile?.let(function::addOriginatingKSFile)
        FileSpec.builder(name.packageName, "${name.simpleNames.joinToString("_")}_SpikeKoinModule")
            .addFunction(function.build())
            .build()
            .writeTo(codeGenerator, aggregating = false)
    }
}
