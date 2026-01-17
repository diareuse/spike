package spike.compiler.generator

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import spike.graph.GraphEntryPoint

class EntryPointTypeConstructor : TypeGenerator<GraphEntryPoint> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint>): TypeSpec.Builder {
        val containerClassName = chain.resolver.getDependencyContainerClassName(chain.subject.type)
        val constructor = FunSpec.Companion.constructorBuilder()
            .addParameter("container", containerClassName)
        chain.spec.primaryConstructor(constructor.build())
        val property = PropertySpec.Companion.builder("container", containerClassName)
            .addModifiers(KModifier.PRIVATE)
            .initializer("container")
        chain.spec.addProperty(property.build())
        return chain.proceed()
    }
}