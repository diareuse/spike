package spike.compiler.processor.symbol

import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import spike.compiler.diagnostic.Involved
import spike.compiler.processor.asOrigin
import spike.compiler.processor.described

class UnresolvedReference(val type: String, private val node: KSNode, private val owner: KSDeclaration) {
    val user: Involved
        get() = Involved(owner.usage(), located()?.asOrigin())

    private fun located() = generateSequence(node) { it.parent }.firstOrNull { it.location is FileLocation }

    private fun KSDeclaration.usage(): String {
        if (this !is KSFunctionDeclaration || !isConstructor()) return described
        return "constructor of ${parentDeclaration?.simpleName?.asString()}"
    }
}
