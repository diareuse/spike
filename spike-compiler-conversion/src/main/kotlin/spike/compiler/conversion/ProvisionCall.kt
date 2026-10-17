package spike.compiler.conversion

import com.squareup.kotlinpoet.CodeBlock

/**
 * Builds the statement that returns the provided value from where the @Provides is declared.
 */
fun interface ProvisionCall {
    fun statement(provision: Provision): CodeBlock
}
