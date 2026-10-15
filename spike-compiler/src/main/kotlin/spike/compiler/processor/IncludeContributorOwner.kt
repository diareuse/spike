package spike.compiler.processor

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

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
        error(
            """@Include $kind ${simpleName.asString()} is declared in class ${owner.simpleName.asString()}, Spike cannot call it:
                |
                |<description>
                |  Spike has no instance of ${owner.simpleName.asString()} to call ${simpleName.asString()} on. Declare it at
                |  top level or inside an object, such as a companion object.
                |</description>""".trimMargin()
        )
    }
}
