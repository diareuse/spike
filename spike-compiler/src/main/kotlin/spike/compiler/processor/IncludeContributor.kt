package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

interface IncludeContributor {
    fun contribute(context: GraphContext, annotated: KSClassDeclaration)
    fun contribute(context: GraphContext, annotated: KSFunctionDeclaration)
}
