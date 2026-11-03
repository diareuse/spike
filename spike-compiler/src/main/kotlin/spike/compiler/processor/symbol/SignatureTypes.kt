package spike.compiler.processor.symbol

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import spike.Include

class SignatureType(val node: KSNode, val type: KSType, val owner: KSDeclaration)

fun Sequence<KSTypeReference>.resolved(owner: KSDeclaration) = map { SignatureType(it, it.resolve(), owner) }

fun interface SignatureTypes {
    fun of(symbol: KSAnnotated): Sequence<SignatureType>

    companion object {
        @Include
        val SignatureTypesDeclaration.types: SignatureTypes
            get() = this
    }
}
