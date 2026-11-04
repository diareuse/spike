package spike.compiler.dagger

import spike.compiler.conversion.loading.SymbolProcessorProviderLoading

class DaggerSymbolProcessorProvider : SymbolProcessorProviderLoading(DaggerProcessorFactory::class.java)
