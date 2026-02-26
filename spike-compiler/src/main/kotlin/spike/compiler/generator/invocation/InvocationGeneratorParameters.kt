package spike.compiler.generator.invocation

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.CodeBlockGenerator
import spike.compiler.generator.CodeBlockGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory

class InvocationGeneratorParameters : CodeBlockGenerator<TypeFactory.Callable> {
    override fun generate(chain: CodeBlockGeneratorChain<TypeFactory.Callable>): CodeBlock.Builder {
        return chain.proceed().also { block ->
            for ((index, param) in chain.subject.invocation.parameters.withIndex()) {
                if (index > 0)
                    block.add(", ")
                block.add("%N = ", param.name)
                block.constructParameter(param.type, chain.resolver)
            }
        }
    }

    private fun CodeBlock.Builder.constructParameter(type: Type, resolver: TypeResolver) {
        if (type is Type.Parametrized) {
            when (type.envelope) {
                resolver.builtInType.Provider -> {
                    val argument = type.typeArguments.single().apply {
                        checkNotNestingProviders(resolver)
                    }
                    add("::%N", resolver.getFieldName(argument))
                }

                resolver.builtInType.Lazy -> {
                    val argument = type.typeArguments.single().apply {
                        checkNotNestingProviders(resolver)
                    }
                    add("%M(::%N)", resolver.builtInMember { lazy }, resolver.getFieldName(argument))
                }

                else -> add("%N", resolver.getFieldName(type))
            }
        } else {
            add("%N", resolver.getFieldName(type))
        }
    }

    private fun Type.checkNotNestingProviders(resolver: TypeResolver) {
        if (this !is Type.Parametrized) return
        check(envelope !in listOf(resolver.builtInType.Provider, resolver.builtInType.Lazy)) {
            "Constructs similar to Provider<Provider<T>> are not supported. Use Provider<T> or Lazy<T> instead. Faulty type: $this"
        }
    }
}