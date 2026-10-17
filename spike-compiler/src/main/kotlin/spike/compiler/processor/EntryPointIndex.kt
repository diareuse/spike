package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ClassName
import spike.compiler.graph.escapedName

object EntryPointIndex {
    const val PACKAGE = "spike.aggregated"
    const val CLASS = "c:"
    const val FUNCTION = "f:"
    const val PROPERTY = "p:"

    fun classNameOf(entryPoint: String) = ClassName(PACKAGE, entryPoint.escapedName())

    fun nameOf(declaration: KSAnnotated): String? = declaration.accept(IndexNameVisitor(), Unit)
}
