package spike.compiler.jsr

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class JsrProcessorFactory : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) = JsrConversion(environment).processor
}
