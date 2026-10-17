package spike.compiler.conversion

import com.squareup.kotlinpoet.CodeBlock

class ProvisionCallTopLevel : ProvisionCall {
    override fun statement(provision: Provision): CodeBlock = CodeBlock.of("return %L", provision.call())
}
