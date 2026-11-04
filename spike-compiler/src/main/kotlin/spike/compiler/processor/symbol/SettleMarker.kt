package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import spike.Include

@Include
class SettleMarker(private val codeGenerator: CodeGenerator) {
    fun write(round: Int) {
        codeGenerator.createNewFile(Dependencies(true), Package, "SpikeRound$round")
            .use { it.write("package $Package\n".toByteArray()) }
    }

    private companion object {
        const val Package = "spike.aggregated"
    }
}
