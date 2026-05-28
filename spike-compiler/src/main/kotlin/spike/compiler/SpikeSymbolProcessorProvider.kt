package spike.compiler

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import spike.compiler.processor.SpikeSymbolProcessor
import spike.compiler.processor.symbol.SymbolProcessorViewModel
import spike.compiler.processor.symbol.SymbolRegistry

class SpikeSymbolProcessorProvider : SymbolProcessorProvider {
    private val registry = SymbolRegistry()
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = SpikeSymbolProcessor(
        viewModel = SymbolProcessorViewModel(environment, registry),
        environment = environment,
        registry = registry
    )
}
