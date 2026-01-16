package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.graph.TypeFactory

class InvocationGeneratorMethod : CodeBlockGenerator<TypeFactory.Callable> {
    override fun generate(chain: CodeBlockGeneratorChain<TypeFactory.Callable>): CodeBlock.Builder {
        val subject = chain.subject as TypeFactory.Method
        chain.spec.add("%M(", chain.resolver.getMemberName(subject.member))
        return chain.proceed().also {
            chain.spec.add(")")
        }
    }
}