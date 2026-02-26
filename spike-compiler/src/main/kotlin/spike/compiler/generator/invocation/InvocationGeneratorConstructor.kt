package spike.compiler.generator.invocation

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.CodeBlockGenerator
import spike.compiler.generator.CodeBlockGeneratorChain
import spike.compiler.graph.TypeFactory

class InvocationGeneratorConstructor : CodeBlockGenerator<TypeFactory.Callable> {
    override fun generate(chain: CodeBlockGeneratorChain<TypeFactory.Callable>): CodeBlock.Builder {
        if (chain.subject !is TypeFactory.Class) return chain.proceed()
        chain.spec.add("%T(", chain.resolver.getTypeName(chain.subject.type))
        return chain.proceed().also {
            chain.spec.add(")")
        }
    }
}