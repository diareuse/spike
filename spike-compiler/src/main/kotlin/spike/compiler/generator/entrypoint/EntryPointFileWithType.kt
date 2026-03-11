package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.FileGenerator
import spike.compiler.generator.FileGeneratorChain
import spike.compiler.generator.GeneratorChain
import spike.compiler.generator.GeneratorChainOrigin
import spike.compiler.graph.GraphEntryPoint

class EntryPointFileWithType(
    private val type: GeneratorChainOrigin<GraphEntryPoint, TypeSpec.Builder>,
) : FileGenerator<GraphEntryPoint> {
    override fun generate(chain: FileGeneratorChain<GraphEntryPoint>): FileSpec.Builder {
        chain.spec.addType(type.proceed().build())
        return chain.proceed(this)
    }
}
