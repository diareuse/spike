package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.code.addDependencyFactoryCall
import spike.compiler.generator.code.addProvider
import spike.compiler.graph.TypeFactory
import spike.BindTarget
import spike.Include

@Include
class TypeFactoryCodeProvides : TypeFactoryCode<TypeFactory.Provides> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Provides) {
        body.addProvider {
            addDependencyFactoryCall(factory.factory)
        }.addStatement("")
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @TypeFactoryVariant(TypeFactory.Provides::class)
        val TypeFactoryCodeProvides.asCode: TypeFactoryCode<*>
            get() = this
    }
}
