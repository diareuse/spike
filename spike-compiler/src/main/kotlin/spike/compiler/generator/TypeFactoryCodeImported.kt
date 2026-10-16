package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.graph.TypeFactory

class TypeFactoryCodeImported : TypeFactoryCode<TypeFactory.Imported> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Imported) {
        body.addStatement("factory.%L", factory.name)
    }
}
