package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.graph.TypeFactory

class InvocationGeneratorCaller : CodeBlockGenerator<TypeFactory.Callable> {
    override fun generate(chain: CodeBlockGeneratorChain<TypeFactory.Callable>): CodeBlock.Builder {
        if (chain.subject.singleton)
            chain.spec.beginControlFlow("%M {", chain.resolver.builtInMember { lazy })
        else chain.spec.add("return ")
        return chain.proceed().also {
            if (chain.subject.singleton) {
                chain.spec.add("\n")
                chain.spec.endControlFlow()
            }
        }
    }
}