package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.Order

/**
 * Falls back to [cold] on any failure, logged at info because a warning fails builds with allWarningsAsErrors.
 */
class ProcessorSourceFallback(
    private val origin: ProcessorSource,
    private val cold: ProcessorSource,
) : ProcessorSource {
    override fun processor(environment: SymbolProcessorEnvironment) = try {
        origin.processor(environment)
    } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
        environment.logger.info("Spike could not keep its compiler classes loaded: $e")
        cold.processor(environment)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        fun decorator(cold: ProcessorSourceCold) = ProcessorSourceDecorator { ProcessorSourceFallback(it, cold) }
    }
}
