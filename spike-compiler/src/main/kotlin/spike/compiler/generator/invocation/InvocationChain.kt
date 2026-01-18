package spike.compiler.generator.invocation

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.CodeBlockGenerator
import spike.compiler.generator.CodeBlockGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.graph.TypeFactory

data class InvocationChain(
    override val subject: TypeFactory.Callable,
    private val generators: List<CodeBlockGenerator<TypeFactory.Callable>>,
    override val resolver: TypeResolver,
    override val spec: CodeBlock.Builder = CodeBlock.builder(),
    private val index: Int = 0
) : CodeBlockGeneratorChain<TypeFactory.Callable> {
    override fun proceed(): CodeBlock.Builder {
        if (index == generators.size) return spec
        return generators[index].generate(copy(index = index + 1))
    }
}