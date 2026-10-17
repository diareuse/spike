package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.visitor.KSDefaultVisitor

/**
 * Reads the type an included declaration provides: a class itself, or what a function or property returns.
 */
class ProvidedTypeVisitor : KSDefaultVisitor<Unit, KSType?>(enableNewFeatures = true) {
    override fun defaultHandler(node: KSNode, data: Unit): KSType? = null

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) =
        classDeclaration.asStarProjectedType()

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) =
        function.returnType?.resolve()

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) = property.type.resolve()
}
