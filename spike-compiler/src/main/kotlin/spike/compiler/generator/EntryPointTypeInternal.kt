package spike.compiler.generator

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import spike.graph.GraphEntryPoint

class EntryPointTypeInternal : TypeGenerator<GraphEntryPoint> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint>): TypeSpec.Builder {
        chain.spec.addModifiers(KModifier.INTERNAL)
        return chain.proceed()
    }
}