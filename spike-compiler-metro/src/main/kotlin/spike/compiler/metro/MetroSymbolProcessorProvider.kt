package spike.compiler.metro

import spike.compiler.conversion.loading.SymbolProcessorProviderLoading

class MetroSymbolProcessorProvider : SymbolProcessorProviderLoading(MetroProcessorFactory::class.java)
