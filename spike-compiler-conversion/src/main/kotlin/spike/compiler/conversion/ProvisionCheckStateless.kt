package spike.compiler.conversion

import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration

/**
 * Rejects a provision whose declaring type Spike cannot instantiate without state: only top-level declarations,
 * objects and interfaces without abstract members can be called.
 */
class ProvisionCheckStateless(private val code: String, private val message: String) : ConversionCheck<Provision> {
    override fun check(subject: Provision): ConversionProblem? {
        val parent = subject.parent
        if (parent == null || parent.classKind == ClassKind.OBJECT || isStatelessInterface(parent)) return null
        return ConversionProblem(code, message, subject.declaration)
    }

    private fun isStatelessInterface(parent: KSClassDeclaration) = parent.classKind == ClassKind.INTERFACE &&
        parent.getAllProperties().none { it.isAbstract() } && parent.getAllFunctions().none { it.isAbstract }
}
