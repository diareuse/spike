package spike.compiler.conversion

import com.google.devtools.ksp.symbol.ClassKind
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ksp.toClassName

class ProvisionCallObject(private val fallback: ProvisionCall) : ProvisionCall {
    override fun statement(provision: Provision): CodeBlock {
        val parent = provision.parent?.takeIf { it.classKind == ClassKind.OBJECT }
            ?: return fallback.statement(provision)
        return CodeBlock.of("return with(%T) { %L }", parent.toClassName(), provision.call())
    }
}
