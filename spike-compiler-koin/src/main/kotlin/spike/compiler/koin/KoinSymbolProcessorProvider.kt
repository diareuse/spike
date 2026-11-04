package spike.compiler.koin

import spike.compiler.conversion.loading.SymbolProcessorProviderLoading

class KoinSymbolProcessorProvider : SymbolProcessorProviderLoading(KoinProcessorFactory::class.java)
