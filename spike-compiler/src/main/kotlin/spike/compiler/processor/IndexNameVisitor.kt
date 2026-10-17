package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.visitor.KSDefaultVisitor

/**
 * Names a declaration in an entry point index: its kind prefix followed by its qualified name.
 */
class IndexNameVisitor : KSDefaultVisitor<Unit, String?>(enableNewFeatures = true) {
    override fun defaultHandler(node: KSNode, data: Unit): String? = null

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) =
        named(EntryPointIndex.CLASS, classDeclaration)

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) =
        named(EntryPointIndex.FUNCTION, function)

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) =
        named(EntryPointIndex.PROPERTY, property)

    private fun named(prefix: String, declaration: KSDeclaration) =
        declaration.qualifiedName?.asString()?.let { prefix + it }
}
