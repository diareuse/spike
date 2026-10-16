package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.code.addOptionalSingleton
import spike.compiler.generator.code.addParameters
import spike.compiler.generator.code.addType
import spike.compiler.graph.TypeFactory

class TypeFactoryCodeClass : TypeFactoryCode<TypeFactory.Class> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Class) {
        body.addOptionalSingleton(context.getDependencyId(factory), factory.singleton) {
            addType(factory.type) {
                body.addParameters(factory.invocation)
            }.addStatement("")
        }
    }
}
