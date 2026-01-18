package spike.compiler.generator.entrypoint

import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.graph.GraphEntryPoint

class EntryPointTypeSuperinterface : TypeGenerator<GraphEntryPoint> {
    override fun generate(chain: TypeGeneratorChain<GraphEntryPoint>): TypeSpec.Builder {
        chain.spec.addSuperinterface(chain.resolver.getTypeName(chain.subject.type))
        return chain.proceed()
    }
}