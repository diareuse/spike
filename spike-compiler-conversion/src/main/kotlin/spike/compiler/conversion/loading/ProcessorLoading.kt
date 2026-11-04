package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import spike.EntryPoint
import spike.Include
import java.net.URLClassLoader

@EntryPoint
interface ProcessorLoading {
    val source: ProcessorSource

    @EntryPoint.Factory
    interface Factory {
        fun create(implementation: ProcessorImplementation, environment: SymbolProcessorEnvironment): ProcessorLoading
    }

    companion object
}

@Include
fun SymbolProcessorEnvironment.logger(): KSPLogger = logger

@Include
fun warmLoaderOf(): (ProcessorClasspath) -> URLClassLoader =
    { URLClassLoader(it.urls.toTypedArray(), it.parent) }

@Include
fun processorSource(
    warm: ProcessorSourceWarm,
    decorators: Map<Int, ProcessorSourceDecorator>,
): ProcessorSource = decorators.toSortedMap().values.fold(warm as ProcessorSource) { inner, decorator ->
    decorator.decorate(inner)
}

abstract class SymbolProcessorProviderLoading(
    private val implementation: Class<out SymbolProcessorProvider>,
) : SymbolProcessorProvider {
    final override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        ProcessorLoading(ProcessorImplementation(implementation), environment).source.processor(environment)
}
