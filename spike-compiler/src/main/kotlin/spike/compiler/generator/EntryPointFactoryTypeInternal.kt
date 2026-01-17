package spike.compiler.generator

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import spike.graph.GraphEntryPoint

class EntryPointFactoryTypeInternal : TypeGenerator<GraphEntryPoint.Factory> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint.Factory>): TypeSpec.Builder {
        chain.spec.addModifiers(KModifier.INTERNAL)
        return chain.proceed()
    }
}