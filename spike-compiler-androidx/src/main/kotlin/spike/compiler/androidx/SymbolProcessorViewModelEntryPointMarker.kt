package spike.compiler.androidx

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo

class SymbolProcessorViewModelEntryPointMarker(
    private val packageName: String,
    private val codeGenerator: CodeGenerator,
    private val identity: ModuleIdentity,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val module = identity.name(resolver)
        val name = ClassName(packageName, ViewModelNames.EntryPointMarkerPrefix + module)
        FileSpec.builder(name)
            .addType(TypeSpec.objectBuilder(name).addModifiers(KModifier.INTERNAL).build())
            .build()
            .writeTo(codeGenerator, aggregating = true)
        return emptyList()
    }
}
