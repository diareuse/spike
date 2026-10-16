package spike.compiler.generator

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import spike.factory.DependencyId

/**
 * Splits `get` into one function per [size] identifiers once it would grow past what the JIT compiles, and routes
 * to them by identifier range.
 */
class DispatchGetChunked(
    private val size: Int,
    private val delegate: DispatchFunctions,
) : DispatchFunctions {
    override fun of(branches: List<ProgramBranch>): List<FunSpec> {
        val chunks = branches.groupBy { it.id / size }
        if (chunks.size <= 1) return delegate.of(branches)
        val router = CodeBlock.builder()
            .beginControlFlow("return when (id.%L / %L) {", DependencyId::id.name, size)
        for (chunk in chunks.keys.sorted()) router.addStatement("%L -> get%L(id)", chunk, chunk)
        router.addStatement($$"else -> error(\"Invalid identifier $id\")")
        router.unindent().add("} as T\n")
        return chunks.map { (chunk, chunkBranches) -> chunk(chunk, chunkBranches) } +
            DispatchGet.getFunction(router.build())
    }

    private fun chunk(chunk: Int, branches: List<ProgramBranch>): FunSpec {
        val block = CodeBlock.builder().beginControlFlow("return when (id.%L) {", DependencyId::id.name)
        for (branch in branches) block.addStatement("%L -> %T.resolve(this)", branch.id, branch.program)
        block.addStatement($$"else -> error(\"Invalid identifier $id\")")
        block.unindent().add("}\n")
        return FunSpec.builder("get$chunk")
            .addModifiers(KModifier.PRIVATE)
            .returns(ANY.copy(nullable = true))
            .addParameter("id", DependencyId::class)
            .addCode(block.build())
            .build()
    }
}
