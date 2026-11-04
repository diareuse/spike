package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import spike.Include
import spike.compiler.conversion.WritingPrediction
import java.util.ServiceLoader

fun interface ProcessorWriters {
    fun mayWrite(resolver: Resolver): Boolean
}

@Include
class ProcessorWritersDiscovered : ProcessorWriters {
    override fun mayWrite(resolver: Resolver): Boolean {
        val loader = javaClass.classLoader
        val predictions = ServiceLoader.load(WritingPrediction::class.java, loader).associateBy { it.provider }
        return ServiceLoader.load(SymbolProcessorProvider::class.java, loader).stream()
            .map { predictions[it.type().name] }
            .anyMatch { it == null || it.writes(resolver) }
    }
}

class ProcessorWritersGuarded(
    private val origin: ProcessorWriters,
    private val logger: KSPLogger,
) : ProcessorWriters {
    override fun mayWrite(resolver: Resolver) = try {
        origin.mayWrite(resolver)
    } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
        logger.info("Spike waits for other processors because it could not tell whether they write: $e")
        true
    }
}

class ProcessorWritersSeparated(
    private val origin: ProcessorWriters,
    private val own: ClassLoader,
    private val api: ClassLoader,
) : ProcessorWriters {
    override fun mayWrite(resolver: Resolver) = own === api || origin.mayWrite(resolver)
}

@Include
fun processorWriters(discovered: ProcessorWritersDiscovered, logger: KSPLogger): ProcessorWriters =
    ProcessorWritersGuarded(
        ProcessorWritersSeparated(
            origin = discovered,
            own = discovered.javaClass.classLoader,
            api = SymbolProcessorProvider::class.java.classLoader,
        ),
        logger,
    )
