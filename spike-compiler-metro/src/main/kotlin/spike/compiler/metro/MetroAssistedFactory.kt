package spike.compiler.metro

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toClassName
import spike.compiler.conversion.Included

/**
 * An @AssistedFactory interface, implemented by forwarding its single abstract function to a Spike function type.
 */
class MetroAssistedFactory(override val declaration: KSClassDeclaration) : Included {
    override val packageName get() = declaration.toClassName().packageName
    override val fileName get() = declaration.toClassName().simpleNames.joinToString("") + "_SpikeAssistedFactory"

    val create get() = declaration.getDeclaredFunctions().singleOrNull { it.isAbstract }

    companion object {
        fun of(symbol: KSAnnotated) = (symbol as? KSClassDeclaration)?.let(::MetroAssistedFactory)
    }
}
