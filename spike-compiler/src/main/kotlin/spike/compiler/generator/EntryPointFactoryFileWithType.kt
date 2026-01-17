package spike.compiler.generator

import com.squareup.kotlinpoet.FileSpec
import spike.graph.GraphEntryPoint

class EntryPointFactoryFileWithType(
    private val type: EntryPointFactoryTypeChain
): FileGenerator<GraphEntryPoint.Factory> {
    override fun generate(chain: FileGeneratorChain<GraphEntryPoint.Factory>): FileSpec.Builder {
        chain.spec.addType(type.proceed().build())
        return chain.proceed()
    }
}