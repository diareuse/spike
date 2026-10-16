package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.graph.TypeFactory

class TypeFactoryCodeProperty : TypeFactoryCode<TypeFactory.Property> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Property) {
        body.addStatement("factory._%L", factory.name)
    }
}
