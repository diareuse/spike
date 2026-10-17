package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ksp.toClassName

/**
 * A class built by calling one of its constructors with dependencies from the graph.
 */
class Injection(val owner: KSClassDeclaration, val constructor: KSFunctionDeclaration) : Included {
    override val declaration get() = owner
    override val packageName get() = owner.toClassName().packageName
    override val fileName get() = owner.toClassName().simpleNames.joinToString("_") + "_SpikeInclude"

    companion object {
        /**
         * Reads an annotated constructor, or the primary constructor of an annotated class.
         */
        fun of(symbol: KSAnnotated): Injection? {
            val constructor = when (symbol) {
                is KSClassDeclaration -> symbol.primaryConstructor
                is KSFunctionDeclaration -> symbol.takeIf { it.simpleName.asString() == "<init>" }
                else -> null
            }
            val owner = constructor?.parentDeclaration as? KSClassDeclaration ?: return null
            return Injection(owner, constructor)
        }
    }
}
