package spike.compiler.conversion

import com.squareup.kotlinpoet.CodeBlock

/**
 * Binds the receiver of an extension declaration to the declared type, as a @Binds does.
 */
class ProvisionCallReceiver : ProvisionCall {
    override fun statement(provision: Provision): CodeBlock = CodeBlock.of("return receiver")
}
