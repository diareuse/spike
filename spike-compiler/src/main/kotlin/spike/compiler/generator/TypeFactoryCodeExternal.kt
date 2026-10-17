package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.graph.TypeFactory
import spike.BindTarget
import spike.Include

@Include
class TypeFactoryCodeExternal : TypeFactoryCode<TypeFactory.External> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.External) {
        val call = if (factory.isMethod) "()" else ""
        body.addStatement("factory.%L.%L%L", context.resolver.getVariableName(factory.origin), factory.name, call)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @TypeFactoryVariant(TypeFactory.External::class)
        val TypeFactoryCodeExternal.asCode: TypeFactoryCode<*>
            get() = this
    }
}
