package spike.compiler

import spike.compiler.conversion.loading.SymbolProcessorProviderLoading

class SpikeSymbolProcessorProvider : SymbolProcessorProviderLoading(SpikeProcessorFactory::class.java)
