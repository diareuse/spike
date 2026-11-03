package spike.compiler.androidx

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class SymbolProcessorValidLibraryOption(
    private val options: Map<String, String>,
    private val logger: KSPLogger,
    private val delegate: SymbolProcessor,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val value = options[ViewModelNames.LibraryOption]
        if (value == null || value.toBooleanStrictOrNull(ignoreCase = true) != null) return delegate.process(resolver)
        logger.error(
            "[SPIKE-LIBRARY-OPTION] The KSP option ${ViewModelNames.LibraryOption}, set by spike { library } or " +
                "ksp { arg(\"${ViewModelNames.LibraryOption}\", ...) }, must be true or false, but is \"$value\"."
        )
        return emptyList()
    }

    private fun String.toBooleanStrictOrNull(ignoreCase: Boolean): Boolean? = when {
        equals("true", ignoreCase) -> true
        equals("false", ignoreCase) -> false
        else -> null
    }
}
