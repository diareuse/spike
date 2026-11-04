package spike.compiler.androidx

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class ViewModelProcessorFactory : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) = ViewModelConversion(environment).processor
}
