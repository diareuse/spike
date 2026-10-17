package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.graph.TypeFactory
import spike.BindTarget
import spike.Include

@Include
class TypeFactoryCodeImported : TypeFactoryCode<TypeFactory.Imported> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Imported) {
        body.addStatement("factory.%L", factory.name)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @TypeFactoryVariant(TypeFactory.Imported::class)
        val TypeFactoryCodeImported.asCode: TypeFactoryCode<*>
            get() = this
    }
}
