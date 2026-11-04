package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.withIndent

inline fun CodeBlock.Builder.addSingleton(index: Int, body: CodeBlock.Builder.() -> Unit) = apply {
    beginControlFlow("factory._singletons.getOrPut(%L) {", index)
    withIndent {
        body()
    }
    endControlFlow()
}

inline fun CodeBlock.Builder.addOptionalSingleton(
    index: Int?,
    body: CodeBlock.Builder.() -> Unit
) = apply {
    if (index != null) addSingleton(index, body)
    else body()
}
