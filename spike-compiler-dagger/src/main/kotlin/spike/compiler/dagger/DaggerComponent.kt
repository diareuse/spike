package spike.compiler.dagger

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import spike.compiler.conversion.EntryPointSubject

class DaggerComponent(override val declaration: KSClassDeclaration) : EntryPointSubject {
    val nested get() = declaration.declarations.filterIsInstance<KSClassDeclaration>()
    val factory get() = nested.firstOrNull(DaggerNames.ComponentFactory::annotates)
    override val entryPoint: ClassName
        get() {
            val name = declaration.toClassName()
            return ClassName(name.packageName, name.simpleNames.joinToString("") + "Spike")
        }

    companion object {
        fun of(symbol: KSAnnotated) = (symbol as? KSClassDeclaration)?.let(::DaggerComponent)
    }
}
