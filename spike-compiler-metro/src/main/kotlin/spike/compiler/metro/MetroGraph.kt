package spike.compiler.metro

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import spike.compiler.conversion.EntryPointSubject

class MetroGraph(override val declaration: KSClassDeclaration) : EntryPointSubject {
    override val entryPoint: ClassName
        get() {
            val name = declaration.toClassName()
            return ClassName(name.packageName, name.simpleNames.joinToString("") + "Spike")
        }

    val factory
        get() = declaration.declarations
            .filterIsInstance<KSClassDeclaration>()
            .firstOrNull(MetroNames.DependencyGraphFactory::annotates)
            ?.getDeclaredFunctions()
            ?.singleOrNull { it.isAbstract }

    val inputs get() = factory?.parameters.orEmpty()

    companion object {
        fun of(symbol: KSAnnotated) = (symbol as? KSClassDeclaration)?.let(::MetroGraph)
    }
}
