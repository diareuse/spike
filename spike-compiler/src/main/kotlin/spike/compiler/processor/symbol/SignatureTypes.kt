package spike.compiler.processor.symbol

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
import spike.Include

class SignatureType(val node: KSNode, val type: KSType)

fun interface SignatureTypes {
    fun of(symbol: KSAnnotated): Sequence<SignatureType>

    companion object {
        @Include
        val SignatureTypesDeclaration.types: SignatureTypes
            get() = this
    }
}
