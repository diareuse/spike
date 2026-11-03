package spike.compiler.processor.symbol

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSTypeReference
import spike.Include

fun interface SignatureReferences {
    fun of(symbol: KSAnnotated): Sequence<KSTypeReference>

    companion object {
        @Include
        val SignatureReferencesDeclaration.references: SignatureReferences
            get() = this
    }
}
