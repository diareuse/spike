package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.Order

class ProcessorSourceHost(
    private val origin: ProcessorSource,
    private val cold: ProcessorSource,
    private val host: HostSystem,
) : ProcessorSource {
    override fun processor(environment: SymbolProcessorEnvironment) = if (host.locksOpenFiles()) {
        environment.logger.info("Spike keeps its compiler classes to this run: this system locks jars a loader holds open")
        cold.processor(environment)
    } else {
        origin.processor(environment)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(15)
        fun decorator(cold: ProcessorSourceCold, host: HostSystem) =
            ProcessorSourceDecorator { ProcessorSourceHost(it, cold, host) }
    }
}
