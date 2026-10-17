package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.code.addDependencyFactoryCall
import spike.compiler.generator.code.addLazy
import spike.compiler.graph.TypeFactory
import spike.BindTarget
import spike.Include

@Include
class TypeFactoryCodeMemorizes : TypeFactoryCode<TypeFactory.Memorizes> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Memorizes) {
        body.addLazy {
            addDependencyFactoryCall(factory.factory)
        }.addStatement("")
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @TypeFactoryVariant(TypeFactory.Memorizes::class)
        val TypeFactoryCodeMemorizes.asCode: TypeFactoryCode<*>
            get() = this
    }
}
