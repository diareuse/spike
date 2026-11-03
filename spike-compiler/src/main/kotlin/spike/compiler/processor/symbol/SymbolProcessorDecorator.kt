package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.SymbolProcessor

fun interface SymbolProcessorDecorator {
    fun decorate(delegate: SymbolProcessor): SymbolProcessor
}
