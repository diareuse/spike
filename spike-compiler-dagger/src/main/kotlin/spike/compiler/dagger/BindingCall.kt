package spike.compiler.dagger

import com.squareup.kotlinpoet.CodeBlock

/**
 * Decides how the generated @Include function reaches a Dagger binding, and how its name starts.
 */
interface BindingCall {
    fun prefix(binding: DaggerBinding): String
    fun statement(binding: DaggerBinding, arguments: CodeBlock): CodeBlock
}
