package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.Include
import spike.compiler.graph.TypeFactory
import kotlin.reflect.KClass

class TypeFactoryExpression(private val codes: Map<KClass<out TypeFactory>, TypeFactoryCode<*>>) {

    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    fun write(body: CodeBlock.Builder) {
        val factory = step.factory
        val code = codes[factory::class] ?: error("No code is registered for ${factory::class.simpleName}")
        @Suppress("UNCHECKED_CAST")
        (code as TypeFactoryCode<TypeFactory>).write(body, factory)
    }

    companion object {
        @Include
        fun default() = TypeFactoryExpression(
            mapOf(
                TypeFactory.Class::class to TypeFactoryCodeClass(),
                TypeFactory.Method::class to TypeFactoryCodeMethod(),
                TypeFactory.Memorizes::class to TypeFactoryCodeMemorizes(),
                TypeFactory.Provides::class to TypeFactoryCodeProvides(),
                TypeFactory.MultibindsCollection::class to TypeFactoryCodeMultibindsCollection(),
                TypeFactory.MultibindsMap::class to TypeFactoryCodeMultibindsMap(),
                TypeFactory.Concatenation::class to TypeFactoryCodeConcatenation(),
                TypeFactory.Argument::class to TypeFactoryCodeArgument(),
                TypeFactory.Assisted::class to TypeFactoryCodeAssisted(),
                TypeFactory.Property::class to TypeFactoryCodeProperty(),
                TypeFactory.External::class to TypeFactoryCodeExternal(),
                TypeFactory.Imported::class to TypeFactoryCodeImported(),
            ),
        )
    }
}
