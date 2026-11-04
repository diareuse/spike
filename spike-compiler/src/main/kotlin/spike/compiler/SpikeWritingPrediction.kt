package spike.compiler

import com.google.devtools.ksp.processing.Resolver
import spike.compiler.conversion.WritingPrediction

class SpikeWritingPrediction : WritingPrediction {
    override val provider: String = SpikeSymbolProcessorProvider::class.java.name

    override fun writes(resolver: Resolver) = false
}
