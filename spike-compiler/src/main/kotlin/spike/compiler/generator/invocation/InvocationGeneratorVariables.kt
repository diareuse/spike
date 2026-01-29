package spike.compiler.generator.invocation

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.CodeBlockGenerator
import spike.compiler.generator.CodeBlockGeneratorChain
import spike.graph.TypeFactory

class InvocationGeneratorVariables : CodeBlockGenerator<TypeFactory.Callable> {
    override fun generate(chain: CodeBlockGeneratorChain<TypeFactory.Callable>): CodeBlock.Builder {
        return chain.proceed().also { block ->
            block.indent()
            for ((index, param) in chain.subject.invocation.parameters.withIndex()) {
                if (index > 0)
                    block.add(",")
                block.add("\n")
                block.add("%N = %N", param.name, chain.resolver.getFieldName(param.type))
            }
            if (chain.subject.invocation.parameters.isNotEmpty())
                block.add("\n")
            block.unindent()
        }
    }
}