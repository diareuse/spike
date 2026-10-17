package spike.compiler.androidx

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import spike.compiler.conversion.SpikeNames

/**
 * Writes the app's entry point for ViewModels, whose factory gives every ViewModel the same singletons.
 */
class SymbolProcessorViewModelEntryPoint(
    private val packageName: String,
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val name = ClassName(packageName, "ViewModelEntryPointImpl")
        val entryPoint = ClassName.bestGuess(ViewModelNames.ViewModelEntryPoint)
        val factory = TypeSpec.interfaceBuilder(name.nestedClass("Factory"))
            .addSuperinterface(entryPoint.nestedClass("Factory"))
            .addAnnotation(SpikeNames.EntryPointFactory)
            .addAnnotation(ViewModelNames.SharesSingletons)
            .addFunction(
                FunSpec.builder("create")
                    .addModifiers(KModifier.OVERRIDE, KModifier.ABSTRACT)
                    .addParameter("handle", ViewModelNames.SavedStateHandle)
                    .returns(name)
                    .build()
            )
            .build()
        val type = TypeSpec.interfaceBuilder(name)
            .addSuperinterface(entryPoint)
            .addAnnotation(SpikeNames.EntryPoint)
            .addType(factory)
            .addType(TypeSpec.companionObjectBuilder().build())
            .build()
        FileSpec.builder(name)
            .addAnnotation(
                AnnotationSpec.builder(ViewModelNames.OptIn)
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                    .addMember("%T::class", ViewModelNames.InternalSpikeApi)
                    .build()
            )
            .addType(type)
            .build()
            .writeTo(codeGenerator, aggregating = true)
        return emptyList()
    }
}
