package spike.compiler.processor.symbol

import com.google.devtools.ksp.symbol.KSAnnotated
import spike.Include
import spike.compiler.diagnostic.Diagnostic

fun interface UnresolvedTypes {
    fun of(symbols: List<KSAnnotated>): List<Diagnostic>

    companion object {
        @Include
        val UnresolvedTypesSignature.unresolved: UnresolvedTypes
            get() = this
    }
}
