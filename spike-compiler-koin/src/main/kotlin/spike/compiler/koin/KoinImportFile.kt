package spike.compiler.koin

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class KoinImportFile(
    private val include: ClassName,
    private val codeGenerator: CodeGenerator,
) : KoinImport {
    override fun write(type: KSType, entryPoint: KSClassDeclaration) {
        val name = "koin_" + type.toClassName().canonicalName.replace('.', '_')
        val function = FunSpec.builder(name)
            .addAnnotation(include)
            .returns(type.toTypeName())
            .addParameter("koin", KoinNames.KoinType)
            .addStatement("return koin.get()")
        entryPoint.containingFile?.let(function::addOriginatingKSFile)
        FileSpec.builder(entryPoint.packageName.asString(), "${name}_SpikeKoin")
            .addFunction(function.build())
            .build()
            .writeTo(codeGenerator, aggregating = false)
    }
}
