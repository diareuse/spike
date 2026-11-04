package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

fun interface ProcessorSource {
    fun processor(environment: SymbolProcessorEnvironment): SymbolProcessor
}

fun interface ProcessorSourceDecorator {
    fun decorate(origin: ProcessorSource): ProcessorSource
}

@JvmInline
value class ProcessorImplementation(val type: Class<out SymbolProcessorProvider>)
