package spike.compiler.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName
import spike.Include
import spike.compiler.graph.TypeFactory
import kotlin.reflect.KClass

@Include
class TypeFactoryExpression(private val codes: Map<KClass<out TypeFactory>, TypeFactoryCode<*>>) {

    context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
    fun write(body: CodeBlock.Builder) {
        codeOf(step.factory).write(body, step.factory)
    }

    fun local(index: Int, type: TypeName, factory: TypeFactory): CodeBlock = codeOf(factory).local(index, type, factory)

    @Suppress("UNCHECKED_CAST")
    private fun codeOf(factory: TypeFactory): TypeFactoryCode<TypeFactory> =
        (codes[factory::class] ?: error("No code is registered for ${factory::class.simpleName}"))
            as TypeFactoryCode<TypeFactory>
}
