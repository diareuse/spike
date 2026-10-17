package spike.compiler.androidx

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ksp.toClassName
import spike.compiler.conversion.Included

/**
 * A @SpikeViewModel class, contributed to the ViewModel map through the constructor Spike would include.
 */
class ViewModelInclusion(
    override val declaration: KSClassDeclaration,
    val constructor: KSFunctionDeclaration,
) : Included {
    override val packageName get() = declaration.toClassName().packageName
    override val fileName get() = declaration.toClassName().simpleNames.joinToString("_") + "_SpikeViewModel"

    companion object {
        fun of(symbol: KSAnnotated): ViewModelInclusion? {
            val viewModel = symbol as? KSClassDeclaration ?: return null
            val constructors = viewModel.declarations.filterIsInstance<KSFunctionDeclaration>()
                .filter { it.simpleName.asString() == "<init>" }
            val constructor = constructors.firstOrNull(ViewModelNames.IncludeConstructor::annotates)
                ?: viewModel.primaryConstructor
            return constructor?.let { ViewModelInclusion(viewModel, it) }
        }
    }
}
