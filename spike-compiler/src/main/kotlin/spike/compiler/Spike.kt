package spike.compiler

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.EntryPoint

@EntryPoint
interface Spike {
    val processor: SymbolProcessor

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): Spike
    }

    companion object
}