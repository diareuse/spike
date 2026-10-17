package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.graph.TypeFactory

class TypeFactoryCodeNull : TypeFactoryCode<TypeFactory.Null> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Null) {
        body.addStatement("null")
    }
}
