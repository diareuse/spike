package spike.compiler.generator.invocation

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.CodeBlockGenerator
import spike.compiler.generator.CodeBlockGeneratorChain
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