package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.code.addCastArgument
import spike.compiler.graph.TypeFactory

class TypeFactoryCodeConcatenation : TypeFactoryCode<TypeFactory.Concatenation> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Concatenation) {
        for (index in factory.parts.indices) {
            if (index > 0) body.add(" + ")
            body.add("(").addCastArgument(index, factory.type).add(")")
        }
        body.addStatement("")
    }
}
