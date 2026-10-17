package spike.compiler.dagger

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.compiler.conversion.Declared

class DaggerModule(override val declaration: KSClassDeclaration, val bindings: List<DaggerBinding>) : Declared {
    companion object {
        fun of(symbol: KSAnnotated): DaggerModule? {
            val module = symbol as? KSClassDeclaration ?: return null
            val companion = module.declarations.filterIsInstance<KSClassDeclaration>()
                .firstOrNull { it.isCompanionObject }
            val functions = module.getDeclaredFunctions() + companion?.getDeclaredFunctions().orEmpty()
            val bindings = functions.filter(DaggerNames.Bindings::annotates).map { DaggerBinding(module, it) }
            return DaggerModule(module, bindings.toList())
        }
    }
}
