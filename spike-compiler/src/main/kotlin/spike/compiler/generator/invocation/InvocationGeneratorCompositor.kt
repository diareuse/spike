package spike.compiler.generator.invocation

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.withIndent
import spike.compiler.generator.CodeBlockGenerator
import spike.compiler.generator.CodeBlockGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.graph.BuiltInMembers
import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory

class InvocationGeneratorCompositor(
    private val componentSubChain: (TypeFactory.Callable) -> InvocationChain
) : CodeBlockGenerator<TypeFactory.Callable> {
    override fun generate(chain: CodeBlockGeneratorChain<TypeFactory.Callable>): CodeBlock.Builder {
        val components = chain.subject.invertDependencyChain().distinctBy { it.type }
        generateComposition(components, chain)
        return chain.proceed()
    }

    private fun generateComposition(components: List<TypeFactory>, chain: CodeBlockGeneratorChain<TypeFactory.Callable>) {
        for (component in components) when (component) {
            is TypeFactory.Callable -> {
                chain.spec.add(
                    "val %N: %T = ",
                    chain.resolver.getFieldName(component.type),
                    chain.resolver.getTypeName(component.type)
                )
                chain.spec.add(componentSubChain(component).proceed().build())
                chain.spec.add("\n")
            }

            is TypeFactory.Binds -> continue
            is TypeFactory.Property -> continue
            is TypeFactory.Deferred -> {
                chain.spec.add("val %N = ", chain.resolver.getFieldName(component.type))
                when (component) {
                    is TypeFactory.Memorizes -> chain.spec.beginControlFlow(
                        "%M {",
                        chain.resolver.builtInMember { BuiltInMembers.lazy })

                    is TypeFactory.Provides -> chain.spec.beginControlFlow(
                        "%T {",
                        chain.resolver.getTypeName(component.type)
                    )
                }
                chain.spec.withIndent {
                    var out = component.factory
                    while (out !is TypeFactory.Callable) {
                        out = out.dependencies.single()
                    }
                    // fixme remove recursion
                    generateComposition(out.invertDependencyChain(), chain)
                    add(componentSubChain(out).proceed().build())
                    add("\n")
                }
                chain.spec.endControlFlow()
            }

            else -> error("Cannot process component (${component::class}) $component")
        }
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

    private fun TypeFactory.invertDependencyChain(): List<TypeFactory> {
        val out = mutableListOf<TypeFactory>()
        val queue = dependencies.toMutableList()
        while (queue.isNotEmpty()) {
            val dependency = queue.removeFirst()
            if (dependency !is TypeFactory.Deferred) {
                queue.addAll(0, dependency.dependencies)
            }
            out.add(0, dependency)
        }
        return out.toList()
    }

}