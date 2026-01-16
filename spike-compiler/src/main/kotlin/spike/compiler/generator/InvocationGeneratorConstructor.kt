package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.graph.TypeFactory

class InvocationGeneratorConstructor : CodeBlockGenerator<TypeFactory.Callable> {
    override fun generate(chain: CodeBlockGeneratorChain<TypeFactory.Callable>): CodeBlock.Builder {
        chain.spec.add("%T(", chain.resolver.getTypeName(chain.subject.type))
        return chain.proceed().also {
            chain.spec.add(")")
        }
    }
}