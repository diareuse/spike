package spike.compiler.processor

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import spike.compiler.diagnostic.DiagnosticException
import spike.compiler.diagnostic.DiagnosticIncludeOwner

class IncludeContributorOwner(
    private val origin: IncludeContributor,
) : IncludeContributor {
    override fun contribute(context: GraphContext, annotated: KSClassDeclaration) =
        origin.contribute(context, annotated)

    override fun contribute(context: GraphContext, annotated: KSFunctionDeclaration) {
        annotated.requireStaticOwner("function")
        origin.contribute(context, annotated)
    }

    override fun contribute(context: GraphContext, annotated: KSPropertyDeclaration) {
        annotated.requireStaticOwner("property")
        origin.contribute(context, annotated)
    }

    private fun KSDeclaration.requireStaticOwner(kind: String) {
        val owner = parentDeclaration as? KSClassDeclaration ?: return
        if (owner.classKind == ClassKind.OBJECT) return
        throw DiagnosticException(
            DiagnosticIncludeOwner(kind, simpleName.asString(), owner.simpleName.asString(), asOrigin())
        )
    }
}
