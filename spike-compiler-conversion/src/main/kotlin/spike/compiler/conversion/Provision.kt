package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.toClassName

/**
 * A @Provides function or property, read the same way whichever of the two it is.
 */
sealed class Provision(override val declaration: KSDeclaration) : Included {
    val parent get() = declaration.parentDeclaration as? KSClassDeclaration
    val name get() = declaration.simpleName.asString()
    val owner get() = parent?.toClassName()?.simpleNames?.joinToString("").orEmpty()
    override val packageName get() = declaration.packageName.asString()
    override val fileName get() = "${owner}_${name}_SpikeProvides"

    abstract val receiver: KSTypeReference?
    abstract val type: KSTypeReference
    abstract val parameters: List<KSValueParameter>

    /**
     * Reads the provided value, on a receiver called `receiver` when the declaration is an extension.
     */
    abstract fun call(): CodeBlock

    protected val target get() = if (receiver != null) "receiver." else ""

    class Function(private val function: KSFunctionDeclaration) : Provision(function) {
        override val receiver get() = function.extensionReceiver
        override val type get() = function.returnType!!
        override val parameters: List<KSValueParameter> get() = function.parameters
        override fun call() = CodeBlock.of(
            "$target%N(%L)",
            name,
            parameters.map { CodeBlock.of("%N", it.name!!.asString()) }.joinToCode(),
        )
    }

    class Property(private val property: KSPropertyDeclaration) : Provision(property) {
        override val receiver get() = property.extensionReceiver
        override val type get() = property.type
        override val parameters get() = emptyList<KSValueParameter>()
        override fun call() = CodeBlock.of("$target%N", name)
    }

    companion object {
        fun of(symbol: KSAnnotated): Provision? = when (symbol) {
            is KSFunctionDeclaration -> Function(symbol)
            is KSPropertyDeclaration -> Property(symbol)
            else -> null
        }
    }
}
