package spike.compiler.generator.invocation

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.CodeBlockGenerator
import spike.compiler.generator.CodeBlockGeneratorChain
import spike.compiler.graph.TypeFactory

class InvocationGeneratorMethod : CodeBlockGenerator<TypeFactory.Callable> {
    override fun generate(chain: CodeBlockGeneratorChain<TypeFactory.Callable>): CodeBlock.Builder {
        val subject = chain.subject as? TypeFactory.Method
            ?: return chain.proceed(this)
        chain.spec.add("%M(", chain.resolver.getMemberName(subject.member))
        return chain.proceed(this).also {
            chain.spec.add(")")
        }
    }
}
