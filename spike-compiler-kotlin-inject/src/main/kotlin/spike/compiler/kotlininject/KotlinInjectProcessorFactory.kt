package spike.compiler.kotlininject

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KotlinInjectProcessorFactory : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) = KotlinInjectConversion(environment).processor
}
