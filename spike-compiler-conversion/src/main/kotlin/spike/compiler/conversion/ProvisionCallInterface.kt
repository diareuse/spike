package spike.compiler.conversion

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ksp.toClassName

class ProvisionCallInterface(private val fallback: ProvisionCall) : ProvisionCall {
    override fun statement(provision: Provision): CodeBlock {
        val parent = provision.parent ?: return fallback.statement(provision)
        return CodeBlock.of("return with(object : %T {}) { %L }", parent.toClassName(), provision.call())
    }
}
