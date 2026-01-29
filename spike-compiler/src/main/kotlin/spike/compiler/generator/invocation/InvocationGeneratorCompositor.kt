package spike.compiler.generator.invocation

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.CodeBlockGenerator
import spike.compiler.generator.CodeBlockGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.graph.Type
import spike.graph.TypeFactory

class InvocationGeneratorCompositor : CodeBlockGenerator<TypeFactory.Callable> {
    override fun generate(chain: CodeBlockGeneratorChain<TypeFactory.Callable>): CodeBlock.Builder {
        for (param in chain.subject.invocation.parameters.distinctBy { chain.resolver.getFieldName(it.type) }) {
            chain.spec.add(
                "val %N: %T = ",
                chain.resolver.getFieldName(param.type),
                chain.resolver.getTypeName(param.type)
            )
            chain.spec.constructInvocation(param.type, chain.resolver)
            chain.spec.add("\n")
        }
        return chain.proceed()
    }

    private fun CodeBlock.Builder.constructInvocation(type: Type, resolver: TypeResolver) {
        if (type is Type.Parametrized) {
            when (type.envelope) {
                resolver.builtInType.Provider -> {
                    add("%T(::", resolver.builtInType { Provider })
                    constructInvocation(type.typeArguments.single(), resolver)
                    add(")")
                    return
                }

                resolver.builtInType.Lazy -> {
                    add("%M(::", resolver.builtInMember { lazy })
                    constructInvocation(type.typeArguments.single(), resolver)
                    add(")")
                    return
                }

                else -> {}
            }
        }
        add("%N", resolver.getFieldName(type))
    }

}