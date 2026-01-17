package spike.compiler.generator

import com.squareup.kotlinpoet.*
import spike.graph.DependencyGraph
import spike.graph.TypeFactory.Companion.contains

class DependencyContainerTypeConstructor : TypeGenerator<DependencyGraph> {
    override fun generate(chain: TypeGeneratorChain<DependencyGraph>): TypeSpec.Builder {
        val factoryParameters = chain.subject.entry.factory?.method?.parameters
        if (factoryParameters != null) {
            val constructor = FunSpec.constructorBuilder().apply {
                for (p in factoryParameters) {
                    val name = chain.resolver.getFieldName(p.type)
                    addParameter(name, chain.resolver.getTypeName(p.type))
                    val ps = PropertySpec
                        .builder(name, chain.resolver.getTypeName(p.type)).initializer(name)
                        .addModifiers(KModifier.PUBLIC)
                    chain.spec.addProperty(ps.build())
                }
            }
            chain.spec.primaryConstructor(constructor.build())
        }
        return chain.proceed()
    }
}

