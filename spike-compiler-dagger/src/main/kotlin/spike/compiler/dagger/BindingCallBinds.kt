package spike.compiler.dagger

import com.squareup.kotlinpoet.CodeBlock

class BindingCallBinds(private val fallback: BindingCall) : BindingCall {
    override fun prefix(binding: DaggerBinding) =
        if (DaggerNames.Binds.annotates(binding.function)) "binds" else fallback.prefix(binding)

    override fun statement(binding: DaggerBinding, arguments: CodeBlock): CodeBlock =
        if (DaggerNames.Binds.annotates(binding.function)) CodeBlock.of("return %L", arguments)
        else fallback.statement(binding, arguments)
}
