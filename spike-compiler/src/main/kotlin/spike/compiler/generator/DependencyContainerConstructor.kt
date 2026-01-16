package spike.compiler.generator

import com.squareup.kotlinpoet.*
import spike.graph.DependencyGraph
import spike.graph.TypeFactory.Companion.contains

class DependencyContainerConstructor : TypeGenerator<DependencyGraph> {
    override fun generate(chain: TypeGeneratorChain<DependencyGraph>): TypeSpec.Builder {
        val factoryParameters = chain.subject.entry.factory?.method?.parameters
        if (factoryParameters != null) {
            val constructor = FunSpec.constructorBuilder().apply {
                for (p in factoryParameters) {
                    addParameter(p.name, chain.resolver.getTypeName(p.type))
                    val name = chain.resolver.getFieldName(p.type)
                    val ps = PropertySpec
                        .builder(name, p.type.toTypeName()).initializer(p.name)
                        .addModifiers(if (p.type in chain.subject.properties) KModifier.PUBLIC else KModifier.PRIVATE)
                    chain.spec.addProperty(ps.build())
                }
            }
            chain.spec.primaryConstructor(constructor.build())
        }
        return chain.proceed()
    }
}

