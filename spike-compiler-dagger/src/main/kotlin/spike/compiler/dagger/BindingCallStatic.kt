package spike.compiler.dagger

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ksp.toClassName

class BindingCallStatic : BindingCall {
    override fun prefix(binding: DaggerBinding) = "provides"

    override fun statement(binding: DaggerBinding, arguments: CodeBlock): CodeBlock {
        val name = binding.function.simpleName.asString()
        return CodeBlock.of("return %T.%N(%L)", binding.module.toClassName(), name, arguments)
    }
}
