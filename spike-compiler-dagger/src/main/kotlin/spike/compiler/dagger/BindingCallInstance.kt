package spike.compiler.dagger

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ksp.toClassName

class BindingCallInstance(private val fallback: BindingCall) : BindingCall {
    override fun prefix(binding: DaggerBinding) = fallback.prefix(binding)

    override fun statement(binding: DaggerBinding, arguments: CodeBlock): CodeBlock {
        if (!needsInstance(binding)) return fallback.statement(binding, arguments)
        val name = binding.function.simpleName.asString()
        return CodeBlock.of("return %T().%N(%L)", binding.module.toClassName(), name, arguments)
    }

    private fun needsInstance(binding: DaggerBinding) = binding.isDeclaredInModule && !binding.isInObject
}
