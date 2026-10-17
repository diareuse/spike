package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.graph.TypeFactory
import spike.BindTarget
import spike.Include

@Include
class TypeFactoryCodeArgument : TypeFactoryCode<TypeFactory.Argument> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Argument) {
        body.addStatement("arguments[%L] as %T", factory.index, context.resolver.getTypeName(factory.type))
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @TypeFactoryVariant(TypeFactory.Argument::class)
        val TypeFactoryCodeArgument.asCode: TypeFactoryCode<*>
            get() = this
    }
}
