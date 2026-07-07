package spike.compiler

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.EntryPoint
import spike.compiler.generator.TypeResolver

@EntryPoint
interface Spike {
    val processor: SymbolProcessor
    val resolver: TypeResolver

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): Spike
    }

    companion object
}