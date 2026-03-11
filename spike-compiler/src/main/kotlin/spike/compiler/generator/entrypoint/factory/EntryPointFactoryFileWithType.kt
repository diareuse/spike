package spike.compiler.generator.entrypoint.factory

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.FileGenerator
import spike.compiler.generator.FileGeneratorChain
import spike.compiler.generator.GeneratorChain
import spike.compiler.generator.GeneratorChainOrigin
import spike.compiler.graph.GraphEntryPoint

class EntryPointFactoryFileWithType(
    private val type: GeneratorChainOrigin<GraphEntryPoint.Factory, TypeSpec.Builder>,
) : FileGenerator<GraphEntryPoint.Factory> {
    override fun generate(chain: FileGeneratorChain<GraphEntryPoint.Factory>): FileSpec.Builder {
        chain.spec.addType(type.proceed().build())
        return chain.proceed(this)
    }
}
