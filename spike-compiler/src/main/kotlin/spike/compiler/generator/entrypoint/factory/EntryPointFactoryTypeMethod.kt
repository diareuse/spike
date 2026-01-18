package spike.compiler.generator.entrypoint.factory

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.graph.GraphEntryPoint

class EntryPointFactoryTypeMethod : TypeGenerator<GraphEntryPoint.Factory> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint.Factory>): TypeSpec.Builder {
        val method = chain.subject.method
        val factoryMethod = FunSpec.builder(method.name)
            .returns(chain.resolver.getTypeName(method.returns))
        if (!chain.subject.isVirtual)
            factoryMethod.addModifiers(KModifier.OVERRIDE)
        val body = CodeBlock.builder()
            .add(
                "return %T(%T(",
                chain.resolver.transformClassName(method.returns),
                chain.resolver.getDependencyContainerClassName(method.returns)
            )
        for ((index, param) in method.parameters.withIndex()) {
            if (index > 0) body.add(", ")
            body.add("%N", param.name)
            factoryMethod.addParameter(param.name, chain.resolver.getTypeName(param.type))
        }
        body.add("))")
        factoryMethod.addCode(body.build())
        chain.spec.addFunction(factoryMethod.build())
        return chain.proceed()
    }
}