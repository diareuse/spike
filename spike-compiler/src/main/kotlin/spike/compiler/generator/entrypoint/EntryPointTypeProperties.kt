package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.graph.GraphEntryPoint

class EntryPointTypeProperties : TypeGenerator<GraphEntryPoint> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint>): TypeSpec.Builder {
        for (prop in chain.subject.properties) {
            val getter = FunSpec.getterBuilder()
                .addStatement("return container.%N", chain.resolver.getFieldName(prop.returns))
            val spec = PropertySpec.builder(prop.name, chain.resolver.getTypeName(prop.returns))
                .getter(getter.build())
                .addModifiers(KModifier.OVERRIDE)
                .build()
            chain.spec.addProperty(spec)
        }
        return chain.proceed()
    }
}