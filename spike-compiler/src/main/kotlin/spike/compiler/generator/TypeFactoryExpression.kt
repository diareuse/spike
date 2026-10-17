package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import spike.Include
import spike.compiler.graph.TypeFactory
import kotlin.reflect.KClass

@Include
class TypeFactoryExpression(private val codes: Map<KClass<out TypeFactory>, TypeFactoryCode<*>>) {

    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    fun write(body: CodeBlock.Builder) {
        val factory = step.factory
        val code = codes[factory::class] ?: error("No code is registered for ${factory::class.simpleName}")
        @Suppress("UNCHECKED_CAST")
        (code as TypeFactoryCode<TypeFactory>).write(body, factory)
    }
}
