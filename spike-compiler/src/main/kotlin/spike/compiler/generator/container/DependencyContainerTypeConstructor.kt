package spike.compiler.generator.container

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.graph.DependencyGraph

class DependencyContainerTypeConstructor : TypeGenerator<DependencyGraph> {
    override fun generate(chain: TypeGeneratorChain<DependencyGraph>): TypeSpec.Builder {
        val factoryParameters = chain.subject.entry.factory.method.parameters
        val constructor = FunSpec.constructorBuilder()
        for (p in factoryParameters) {
            val parameterType = chain.resolver.getTypeName(p.type)
            val parameterName = chain.resolver.getFieldName(p.type)
            val parameterModifier =
                if (chain.subject.entry.isRootProperty(p.type)) KModifier.PUBLIC else KModifier.PRIVATE
            val ps = PropertySpec.builder(parameterName, parameterType)
                .initializer(parameterName)
                .addModifiers(parameterModifier)
            chain.spec.addProperty(ps.build())
            constructor.addParameter(parameterName, parameterType)
        }
        chain.spec.primaryConstructor(constructor.build())
        return chain.proceed()
    }
}
