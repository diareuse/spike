package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.withIndent
import spike.compiler.generator.FileGeneratorContext
import spike.compiler.graph.Type
import spike.compiler.graph.TypeFactory
import kotlin.reflect.KClass

context(context: FileGeneratorContext)
inline fun CodeBlock.Builder.addMap(key: Type, value: Type, body: CodeBlock.Builder.() -> Unit) = apply {
    addStatement(
        "%M<%T, %T>(",
        context.resolver.builtInMember { mapOf },
        context.resolver.getTypeName(key),
        context.resolver.getTypeName(value)
    )
    withIndent {
        body()
    }
    add(")")
}

context(context: FileGeneratorContext)
private fun mapEntryKey(key: Any?) = when (key) {
    is String -> "\"$key\""
    is KClass<*> -> "${key.qualifiedName}::class"
    is Type -> context.resolver.getTypeName(key).toString() + "::class"
    else -> key
}

context(context: FileGeneratorContext)
fun CodeBlock.Builder.mapEntries(entries: Iterable<Map.Entry<Any?, TypeFactory>>) = apply {
    for ((index, entry) in entries.withIndex()) context.apply {
        if (index > 0) addStatement(",")
        val (k, v) = entry
        add("%L to ", mapEntryKey(k))
        when (v) {
            is TypeFactory.Memorizes -> addLazy {
                addDependencyFactoryCall(v.factory, v.type.typeArguments.single())
            }
            is TypeFactory.Provides -> addProvider {
                addDependencyFactoryCall(v.factory, v.type.typeArguments.single())
            }
            else -> addBufferCast(index, v.type)
        }
    }
    addStatement("")
}
