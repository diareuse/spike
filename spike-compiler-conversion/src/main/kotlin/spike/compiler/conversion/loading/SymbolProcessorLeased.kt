package spike.compiler.conversion.loading

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class SymbolProcessorLeased(
    private val delegate: SymbolProcessor,
    private val lease: WarmLease,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> = try {
        delegate.process(resolver)
    } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
        lease.evict()
        throw e
    }

    override fun finish() = try {
        delegate.finish()
    } finally {
        lease.release()
    }

    override fun onError() = try {
        delegate.onError()
    } finally {
        lease.evict()
    }
}
