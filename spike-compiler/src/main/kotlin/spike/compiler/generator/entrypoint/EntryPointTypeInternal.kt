package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.graph.GraphEntryPoint

class EntryPointTypeInternal : TypeGenerator<GraphEntryPoint> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint>): TypeSpec.Builder {
        chain.spec.addModifiers(KModifier.INTERNAL)
        return chain.proceed()
    }
}