package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.graph.GraphEntryPoint

class EntryPointTypeFunctions : TypeGenerator<GraphEntryPoint> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint>): TypeSpec.Builder {
        for (method in chain.subject.methods) {
            val spec = FunSpec.Companion.builder(method.name)
                .returns(chain.resolver.getTypeName(method.returns))
                .addModifiers(KModifier.OVERRIDE)
                .addStatement("return container.%N", chain.resolver.getFieldName(method.returns))
                .build()
            chain.spec.addFunction(spec)
        }
        return chain.proceed()
    }
}