package spike.compiler.androidx

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class SymbolProcessorModuleRole(
    private val options: Map<String, String>,
    private val library: SymbolProcessor,
    private val app: SymbolProcessor,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (options[ViewModelNames.LibraryOption].toBoolean()) return library.process(resolver)
        return app.process(resolver)
    }
}
