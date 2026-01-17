package spike.compiler.generator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import spike.graph.GraphEntryPoint

class EntryPointFileWithType(
    private val type: EntryPointTypeChain,
) : FileGenerator<GraphEntryPoint> {
    override fun generate(chain: FileGeneratorChain<GraphEntryPoint>): FileSpec.Builder {
        chain.spec.addType(type.proceed().build())
        return chain.proceed()
    }
}