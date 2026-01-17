package spike.compiler.generator

import com.squareup.kotlinpoet.TypeSpec
import spike.graph.GraphEntryPoint

class EntryPointTypeSuperinterface : TypeGenerator<GraphEntryPoint> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint>): TypeSpec.Builder {
        chain.spec.addSuperinterface(chain.resolver.getTypeName(chain.subject.type))
        return chain.proceed()
    }
}