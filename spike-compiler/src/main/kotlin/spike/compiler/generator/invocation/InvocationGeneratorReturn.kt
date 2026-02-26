package spike.compiler.generator.invocation

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.CodeBlockGenerator
import spike.compiler.generator.CodeBlockGeneratorChain
import spike.compiler.graph.TypeFactory

class InvocationGeneratorReturn : CodeBlockGenerator<TypeFactory.Callable> {
    override fun generate(chain: CodeBlockGeneratorChain<TypeFactory.Callable>): CodeBlock.Builder {
        val factory = chain.subject
        val codeBlock = chain.spec
        if (!factory.singleton) {
            codeBlock.add("return ")
        }
        return chain.proceed()
    }
}