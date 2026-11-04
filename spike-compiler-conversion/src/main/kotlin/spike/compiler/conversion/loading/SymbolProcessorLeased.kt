package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class SymbolProcessorLeased(
    private val delegate: SymbolProcessor,
    private val lease: WarmLease,
    private val failed: () -> Boolean,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> = abandoningOnThrow { delegate.process(resolver) }

    override fun finish() {
        abandoningOnThrow { delegate.finish() }
        if (failed()) lease.evict() else lease.release()
    }

    override fun onError() {
        abandoningOnThrow { delegate.onError() }
        lease.evict()
    }

    private inline fun <T> abandoningOnThrow(block: () -> T): T = try {
        block()
    } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
        lease.abandon()
        throw e
    }
}
