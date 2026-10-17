package spike.compiler.dagger

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import spike.compiler.conversion.Declared

class DaggerBinding(val module: KSClassDeclaration, val function: KSFunctionDeclaration) : Declared {
    override val declaration get() = function
    val isDeclaredInModule get() = function.parentDeclaration == module
    val isInObject get() = module.classKind == ClassKind.OBJECT
}
