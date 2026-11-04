package spike.compiler.androidx

import spike.compiler.conversion.loading.SymbolProcessorProviderLoading

class ViewModelSymbolProcessorProvider : SymbolProcessorProviderLoading(ViewModelProcessorFactory::class.java)
