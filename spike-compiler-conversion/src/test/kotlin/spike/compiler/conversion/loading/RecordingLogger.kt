package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSNode

class RecordingLogger : KSPLogger {
    val infos = mutableListOf<String>()
    val warnings = mutableListOf<String>()

    override fun logging(message: String, symbol: KSNode?) = Unit
    override fun info(message: String, symbol: KSNode?) {
        infos += message
    }

    override fun warn(message: String, symbol: KSNode?) {
        warnings += message
    }

    override fun error(message: String, symbol: KSNode?) = Unit
    override fun exception(e: Throwable) = Unit
}
