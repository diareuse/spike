package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.Order

class ProcessorSourceOption(
    private val origin: ProcessorSource,
    private val cold: ProcessorSource,
) : ProcessorSource {
    override fun processor(environment: SymbolProcessorEnvironment) =
        if (environment.options[Option]?.toBoolean() == false) {
            environment.logger.info("Spike keeps its compiler classes to this run: $Option is false")
            cold.processor(environment)
        } else {
            origin.processor(environment)
        }

    companion object {
        private const val Option = "spike.warm"

        @Include(bindTo = BindTarget.Map)
        @Order(20)
        fun decorator(cold: ProcessorSourceCold) = ProcessorSourceDecorator { ProcessorSourceOption(it, cold) }
    }
}
