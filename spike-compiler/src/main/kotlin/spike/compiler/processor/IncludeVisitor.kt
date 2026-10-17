package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import spike.Include

/**
 * Hands an included declaration to the [IncludeContributor] overload of its kind.
 */
@Include
class IncludeVisitor(
    private val include: IncludeContributor,
) : KSDefaultVisitor<GraphContext, Unit>(enableNewFeatures = true) {
    override fun defaultHandler(node: KSNode, data: GraphContext) = Unit

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: GraphContext) =
        include.contribute(data, classDeclaration)

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: GraphContext) =
        include.contribute(data, function)

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: GraphContext) =
        include.contribute(data, property)
}
