package spike.compiler.generator.entrypoint.factory

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.graph.GraphEntryPoint

class EntryPointFactoryTypeInternal : TypeGenerator<GraphEntryPoint.Factory> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint.Factory>): TypeSpec.Builder {
        chain.spec.addModifiers(KModifier.INTERNAL)
        return chain.proceed()
    }
}