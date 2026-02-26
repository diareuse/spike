package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.FileSpec
import spike.compiler.generator.FileGenerator
import spike.compiler.generator.FileGeneratorChain
import spike.compiler.graph.GraphEntryPoint

class EntryPointFileWithType(
    private val type: EntryPointTypeChain,
) : FileGenerator<GraphEntryPoint> {
    override fun generate(chain: FileGeneratorChain<GraphEntryPoint>): FileSpec.Builder {
        chain.spec.addType(type.proceed().build())
        return chain.proceed()
    }
}