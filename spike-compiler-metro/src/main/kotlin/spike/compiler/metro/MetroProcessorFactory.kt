package spike.compiler.metro

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class MetroProcessorFactory : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) = MetroConversion(environment).processor
}
