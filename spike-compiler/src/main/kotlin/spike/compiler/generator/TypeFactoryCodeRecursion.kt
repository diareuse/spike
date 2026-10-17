package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.code.addDependencyFactoryCall
import spike.compiler.graph.TypeFactory

class TypeFactoryCodeRecursion : TypeFactoryCode<TypeFactory.Recursion> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Recursion) {
        body.addDependencyFactoryCall(factory).addStatement("")
    }
}
