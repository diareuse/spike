package spike.compiler.generator.entrypoint.factory

import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.graph.GraphEntryPoint

class EntryPointFactoryTypeSuperinterface : TypeGenerator<GraphEntryPoint.Factory> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint.Factory>): TypeSpec.Builder {
        if (!chain.subject.isVirtual)
            chain.spec.addSuperinterface(chain.resolver.getTypeName(chain.subject.type))
        return chain.proceed()
    }
}