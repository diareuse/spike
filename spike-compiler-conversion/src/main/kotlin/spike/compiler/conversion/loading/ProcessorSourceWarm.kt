package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import spike.Include
import java.net.URLClassLoader

@Include
class ProcessorSourceWarm(
    private val implementation: ProcessorImplementation,
    private val loaders: WarmLoaders,
) : ProcessorSource {
    override fun processor(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val perRun = implementation.type.classLoader as? URLClassLoader
            ?: error("KSP loaded ${implementation.type.name} outside a URLClassLoader")
        val lease = loaders.lease(ProcessorClasspath(perRun.urLs.toList(), perRun.parent), environment)
        environment.logger.info(if (lease.reused) ReusedMessage else LoadedMessage)
        val logger = KSPLoggerObserved(environment.logger)
        return try {
            SymbolProcessorLeased(created(lease, environment.logging(logger)), lease, logger::failed)
        } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
            lease.evict()
            throw e
        }
    }

    private fun created(lease: WarmLease, environment: SymbolProcessorEnvironment): SymbolProcessor {
        val type = lease.loader.loadClass(implementation.type.name)
        return (type.getDeclaredConstructor().newInstance() as SymbolProcessorProvider).create(environment)
    }

    private companion object {
        const val ReusedMessage = "Spike reused compiler classes that were already loaded"
        const val LoadedMessage = "Spike loaded the compiler classes for this classpath"
    }
}
