package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.Include

@Include
class ProcessorSourceCold(private val implementation: ProcessorImplementation) : ProcessorSource {
    override fun processor(environment: SymbolProcessorEnvironment) =
        implementation.type.getDeclaredConstructor().newInstance().create(environment)
}
