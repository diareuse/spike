package spike.compiler.androidx

import com.google.devtools.ksp.processing.Resolver
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.WritingPrediction

class ViewModelWritingPrediction : WritingPrediction {
    override val provider: String = ViewModelSymbolProcessorProvider::class.java.name

    override fun writes(resolver: Resolver) =
        SymbolSourceAnnotated(ViewModelNames.SpikeViewModel).symbols(resolver).isNotEmpty() ||
            resolver.getClassDeclarationByName(resolver.getKSNameFromString(ViewModelNames.ViewModelEntryPoint)) != null
}
