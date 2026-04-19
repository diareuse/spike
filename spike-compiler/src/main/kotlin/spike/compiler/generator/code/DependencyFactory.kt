package spike.compiler.generator.code

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import spike.compiler.generator.FileGeneratorContext
import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory
import spike.factory.DependencyId

context(context: FileGeneratorContext)
fun CodeBlock.Builder.addDependencyFactoryCall(
    dependencyFactory: ClassName,
    factory: TypeFactory,
    type: Type = factory.type
) = add(
    "%T.get<%T>(%T(%L))",
    dependencyFactory,
    context.resolver.getTypeName(type),
    DependencyId::class,
    context.getDependencyId(factory)
)
