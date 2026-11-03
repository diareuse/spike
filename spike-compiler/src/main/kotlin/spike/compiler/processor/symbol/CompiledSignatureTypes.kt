package spike.compiler.processor.symbol

import com.google.devtools.ksp.symbol.KSAnnotated
import spike.Include

fun interface CompiledSignatureTypes {
    fun of(symbol: KSAnnotated): Sequence<SignatureType>

    companion object {
        @Include
        val CompiledSignatureTypesSpike.types: CompiledSignatureTypes
            get() = this
    }
}
