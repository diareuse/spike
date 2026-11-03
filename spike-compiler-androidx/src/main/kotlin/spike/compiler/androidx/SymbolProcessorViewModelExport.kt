package spike.compiler.androidx

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import spike.compiler.conversion.SymbolSource

class SymbolProcessorViewModelExport(
    private val viewModels: SymbolSource,
    private val codeGenerator: CodeGenerator,
    private val identity: ModuleIdentity,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val declarations = viewModels.symbols(resolver).filterIsInstance<KSClassDeclaration>()
        if (declarations.isEmpty()) return emptyList()
        val packageName = declarations.minOf { it.packageName.asString() }
        val module = identity.name(resolver)
        val name = ClassName(packageName, "${ViewModelNames.ViewModelExports}_$module")
        val type = TypeSpec.interfaceBuilder(name)
            .addAnnotation(ViewModelNames.ExportClass)
            .build()
        FileSpec.builder(name)
            .addType(type)
            .build()
            .writeTo(codeGenerator, aggregating = true, originatingKSFiles = declarations.mapNotNull { it.containingFile })
        return emptyList()
    }
}
