package spike.compiler.generator

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import spike.graph.GraphEntryPoint

class EntryPointTypeProperties : TypeGenerator<GraphEntryPoint> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint>): TypeSpec.Builder {
        for (prop in chain.subject.properties) {
            val getter = FunSpec.Companion.getterBuilder()
                .addStatement("return container.%N", chain.resolver.getFieldName(prop.returns))
            val spec = PropertySpec.Companion.builder(prop.name, chain.resolver.getTypeName(prop.returns))
                .getter(getter.build())
                .addModifiers(KModifier.OVERRIDE)
                .build()
            chain.spec.addProperty(spec)
        }
        return chain.proceed()
    }
}