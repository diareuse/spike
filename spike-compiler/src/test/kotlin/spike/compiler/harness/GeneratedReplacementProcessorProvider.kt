package spike.compiler.harness

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated

class GeneratedReplacementProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) = object : SymbolProcessor {
        private var generated = false

        override fun process(resolver: Resolver): List<KSAnnotated> {
            if (generated) return emptyList()
            generated = true
            environment.codeGenerator.createNewFile(Dependencies(false), "", "ClockReplacement").use {
                it.write("@spike.test.Replaces(Clock::class)\nfun replacingClock(): Clock = Clock()\n".toByteArray())
            }
            return emptyList()
        }
    }
}
