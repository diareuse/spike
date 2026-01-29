package spike.compiler.generator.invocation

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.CodeBlockGenerator
import spike.compiler.generator.CodeBlockGeneratorChain
import spike.graph.BuiltInMembers
import spike.graph.TypeFactory

class InvocationGeneratorDelegation : CodeBlockGenerator<TypeFactory.Callable> {
    override fun generate(chain: CodeBlockGeneratorChain<TypeFactory.Callable>): CodeBlock.Builder {
        val factory = chain.subject
        val codeBlock = chain.spec
        val resolver = chain.resolver
        if (factory.singleton)
            codeBlock.beginControlFlow("%M {", resolver.builtInMember { BuiltInMembers.lazy })
        return chain.proceed().also {
            if (factory.singleton) {
                codeBlock.add("\n")
                codeBlock.endControlFlow()
            }
        }
    }
}