package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.graph.TypeFactory
import spike.BindTarget
import spike.Include

@Include
class TypeFactoryCodeNull : TypeFactoryCode<TypeFactory.Null> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Null) {
        body.addStatement("null")
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @TypeFactoryVariant(TypeFactory.Null::class)
        val TypeFactoryCodeNull.asCode: TypeFactoryCode<*>
            get() = this
    }
}
