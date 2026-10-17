package spike.compiler.dagger

import com.squareup.kotlinpoet.CodeBlock
import spike.BindTarget
import spike.Include
import spike.compiler.conversion.Order

class BindingCallBinds(private val fallback: BindingCall) : BindingCall {
    override fun prefix(binding: DaggerBinding) =
        if (DaggerNames.Binds.annotates(binding.function)) "binds" else fallback.prefix(binding)

    override fun statement(binding: DaggerBinding, arguments: CodeBlock): CodeBlock =
        if (DaggerNames.Binds.annotates(binding.function)) CodeBlock.of("return %L", arguments)
        else fallback.statement(binding, arguments)

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(20)
        fun decorator(): BindingCallDecorator = BindingCallDecorator(::BindingCallBinds)
    }
}
