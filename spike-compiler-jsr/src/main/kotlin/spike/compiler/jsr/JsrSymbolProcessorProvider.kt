package spike.compiler.jsr

import spike.compiler.conversion.loading.SymbolProcessorProviderLoading

class JsrSymbolProcessorProvider : SymbolProcessorProviderLoading(JsrProcessorFactory::class.java)
