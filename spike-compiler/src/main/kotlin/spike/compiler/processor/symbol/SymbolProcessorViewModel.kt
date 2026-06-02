package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import spike.EntryPoint
import spike.Include
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@Include
@OptIn(ExperimentalAtomicApi::class)
class SymbolProcessorViewModel(
    private val environment: SymbolProcessorEnvironment,
    private val registry: SymbolRegistry
) : SymbolProcessor {

    private var processed = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // we generate this entry point in the first round only, then other SymbolProcessors take over
        if (processed) return emptyList()

        val enabled = environment.options["spike.viewmodel.enabled"]?.toBoolean() ?: true
        val entryPointName = resolver.getKSNameFromString("spike.lifecycle.viewmodel.ViewModelEntryPoint")
        val entryPoint = resolver.getClassDeclarationByName(entryPointName)
        if (entryPoint != null && enabled) {
            val packageName = environment.options["spike.viewmodel.package"] ?: "spike.lifecycle.viewmodel"
            val name = ClassName(packageName, "ViewModelEntryPointImpl")
            val factory = TypeSpec.interfaceBuilder(name.nestedClass("Factory"))
                .addSuperinterface(name.peerClass("ViewModelEntryPoint").nestedClass("Factory"))
                .addAnnotation(EntryPoint.Factory::class)
                .addFunction(
                    FunSpec.builder("create")
                        .addModifiers(KModifier.OVERRIDE, KModifier.ABSTRACT)
                        .addParameter("handle", ClassName("androidx.lifecycle", "SavedStateHandle"))
                        .returns(name)
                        .build()
                )
                .build()
            val type = TypeSpec.interfaceBuilder(name)
                .addSuperinterface(name.peerClass("ViewModelEntryPoint"))
                .addAnnotation(EntryPoint::class)
                .addType(factory)
                .addType(TypeSpec.companionObjectBuilder().build())
                .build()
            val file = FileSpec.builder(name).addType(type).build()
            file.writeTo(environment.codeGenerator, true)
            processed = true
            return registry.all(resolver)
        }
        return emptyList()
    }
}
