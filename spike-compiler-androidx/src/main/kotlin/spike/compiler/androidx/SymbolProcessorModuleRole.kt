package spike.compiler.androidx

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class SymbolProcessorModuleRole(
    private val options: Map<String, String>,
    private val logger: KSPLogger,
    private val library: SymbolProcessor,
    private val app: SymbolProcessor,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> = when (val value = options[ViewModelNames.LibraryOption]) {
        null, "false" -> app.process(resolver)
        "true" -> library.process(resolver)
        else -> {
            logger.error(
                "[SPIKE-LIBRARY-OPTION] The KSP option ${ViewModelNames.LibraryOption} must be true or false, " +
                    "but is \"$value\"."
            )
            emptyList()
        }
    }
}
