package spike.compiler.generator.invocation

import com.squareup.kotlinpoet.CodeBlock
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle
import spike.compiler.generator.CodeBlockGenerator
import spike.compiler.generator.CodeBlockGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.graph.TypeFactory

class InvocationChain(
    override val subject: TypeFactory.Callable,
    private val generators: List<CodeBlockGenerator<TypeFactory.Callable>>,
    override val resolver: TypeResolver,
) : CodeBlockGeneratorChain<TypeFactory.Callable> {
    override val spec: CodeBlock.Builder = CodeBlock.builder()
    fun proceed() = generators[0].generate(this)
    override fun proceed(origin: CodeBlockGenerator<TypeFactory.Callable>): CodeBlock.Builder {
        val index = generators.indexOf(origin) + 1
        if (index == generators.size) return spec
        return generators[index].generate(this)
    }
}
