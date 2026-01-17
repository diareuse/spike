package spike.compiler.generator

import com.squareup.kotlinpoet.TypeSpec
import spike.graph.GraphEntryPoint

class EntryPointFactoryTypeSuperinterface : TypeGenerator<GraphEntryPoint.Factory> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint.Factory>): TypeSpec.Builder {
        chain.spec.addSuperinterface(chain.resolver.getTypeName(chain.subject.type))
        return chain.proceed()
    }
}