package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.code.addCall
import spike.compiler.generator.code.addOptionalSingleton
import spike.compiler.graph.TypeFactory
import spike.BindTarget
import spike.Include

@Include
class TypeFactoryCodeMethod : TypeFactoryCode<TypeFactory.Method> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Method) {
        body.addOptionalSingleton(context.getDependencyId(factory), factory.singleton) {
            body.addCall(factory.member, factory.invocation).addStatement("")
        }
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @TypeFactoryVariant(TypeFactory.Method::class)
        val TypeFactoryCodeMethod.asCode: TypeFactoryCode<*>
            get() = this
    }
}
