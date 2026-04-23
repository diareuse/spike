package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.withIndent

inline fun CodeBlock.Builder.addSingleton(index: Int, body: CodeBlock.Builder.() -> Unit) = apply {
    beginControlFlow("singletons.getOrPut(%L) {", index)
    withIndent {
        body()
    }
    endControlFlow()
}

inline fun CodeBlock.Builder.addOptionalSingleton(index: Int, singleton: Boolean, body: CodeBlock.Builder.() -> Unit) = apply {
    if (singleton) addSingleton(index, body)
    else body()
}
