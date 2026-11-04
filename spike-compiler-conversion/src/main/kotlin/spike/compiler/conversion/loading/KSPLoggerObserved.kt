package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSNode

class KSPLoggerObserved(private val origin: KSPLogger) : KSPLogger by origin {
    @Volatile
    var failed = false
        private set

    override fun error(message: String, symbol: KSNode?) {
        failed = true
        origin.error(message, symbol)
    }

    override fun exception(e: Throwable) {
        failed = true
        origin.exception(e)
    }
}

fun SymbolProcessorEnvironment.logging(logger: KSPLogger) = SymbolProcessorEnvironment(
    options = options,
    kotlinVersion = kotlinVersion,
    codeGenerator = codeGenerator,
    logger = logger,
    apiVersion = apiVersion,
    compilerVersion = compilerVersion,
    platforms = platforms,
    kspVersion = kspVersion,
    registerProcessorForNewFeatures = registerProcessorForNewFeatures,
)
