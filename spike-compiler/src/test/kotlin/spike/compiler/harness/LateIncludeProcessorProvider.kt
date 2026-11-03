package spike.compiler.harness

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated

class LateIncludeProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) = object : SymbolProcessor {
        private var round = 0
        private var triggered = false

        override fun process(resolver: Resolver): List<KSAnnotated> {
            round++
            triggered = triggered || resolver.getSymbolsWithAnnotation("GeneratesIncludeLater").any()
            if (!triggered || round > GeneratingRound) return emptyList()
            if (round < GeneratingRound) write("Filler$round", "object Filler$round\n")
            else write("Clock", "@spike.Include\nclass Clock {\n    val name = \"late\"\n}\n")
            return emptyList()
        }

        private fun write(name: String, source: String) {
            environment.codeGenerator.createNewFile(Dependencies(false), "", name).use {
                it.write(source.toByteArray())
            }
        }
    }

    private companion object {
        const val GeneratingRound = 4
    }
}
