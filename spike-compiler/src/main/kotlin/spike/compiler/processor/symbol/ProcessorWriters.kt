package spike.compiler.processor.symbol

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

class ProcessorWritersGuarded(private val origin: ProcessorWriters) : ProcessorWriters {
    override fun mayWrite(resolver: Resolver) = try {
        origin.mayWrite(resolver)
    } catch (@Suppress("TooGenericExceptionCaught") _: Throwable) {
        true
    }
}

@Include
fun processorWriters(discovered: ProcessorWritersDiscovered): ProcessorWriters = ProcessorWritersGuarded(discovered)
