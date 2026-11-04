package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName
import spike.compiler.generator.code.addOptionalSingleton
import spike.compiler.generator.code.addParameters
import spike.compiler.generator.code.addType
import spike.compiler.graph.TypeFactory
import spike.BindTarget
import spike.Include

@Include
class TypeFactoryCodeClass : TypeFactoryCode<TypeFactory.Class> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Class) {
        body.addOptionalSingleton(context.singletonIdOf(factory)) {
            addType(factory.type) {
                body.addParameters(factory.invocation)
            }.addStatement("")
        }
    }

    override fun local(index: Int, type: TypeName, factory: TypeFactory.Class): CodeBlock {
        if (factory.singleton || type !is ClassName || type.isNullable) return super.local(index, type, factory)
        return CodeBlock.of("val v%L = ", index)
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @TypeFactoryVariant(TypeFactory.Class::class)
        val TypeFactoryCodeClass.asCode: TypeFactoryCode<*>
            get() = this
    }
}
