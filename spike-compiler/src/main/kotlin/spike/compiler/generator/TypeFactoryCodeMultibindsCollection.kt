package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.code.addParameters
import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory
import spike.BindTarget
import spike.Include

@Include
class TypeFactoryCodeMultibindsCollection : TypeFactoryCode<TypeFactory.MultibindsCollection> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.MultibindsCollection) {
        body.add(
            "%M<%T>(",
            context.resolver.getMemberName(factory.collectionMemberFactory),
            context.resolver.getTypeName((factory.type as Type.Parametrized).typeArguments.single()),
        )
        body.addParameters(factory.entries)
        body.add(")").addStatement("")
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @TypeFactoryVariant(TypeFactory.MultibindsCollection::class)
        val TypeFactoryCodeMultibindsCollection.asCode: TypeFactoryCode<*>
            get() = this
    }
}
