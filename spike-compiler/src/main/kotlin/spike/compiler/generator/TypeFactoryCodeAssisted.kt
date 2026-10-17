package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.joinToCode
import spike.compiler.graph.TypeFactory
import spike.factory.DependencyId
import spike.BindTarget
import spike.Include

@Include
class TypeFactoryCodeAssisted : TypeFactoryCode<TypeFactory.Assisted> {
    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    override fun write(body: CodeBlock.Builder, factory: TypeFactory.Assisted) {
        val names = factory.arguments.indices.map { "a$it" }
        val parameters = factory.arguments.mapIndexed { index, type ->
            CodeBlock.of("%L: %T", names[index], context.resolver.getTypeName(type))
        }
        body.addStatement(
            "{ %L -> factory.assisted<%T>(%T(%L), arrayOf<Any?>(%L)) }",
            parameters.joinToCode(),
            context.resolver.getTypeName(factory.type.typeArguments.last()),
            DependencyId::class,
            context.getDependencyId(factory),
            names.joinToString(),
        )
    }

    companion object {
        @Include(bindTo = BindTarget.Map)
        @TypeFactoryVariant(TypeFactory.Assisted::class)
        val TypeFactoryCodeAssisted.asCode: TypeFactoryCode<*>
            get() = this
    }
}
