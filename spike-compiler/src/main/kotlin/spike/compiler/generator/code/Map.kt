package spike.compiler.generator.code

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.withIndent
import spike.compiler.generator.FileGeneratorContext
import spike.compiler.generator.LinearReferences
import spike.compiler.generator.LinearStep
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

context(context: FileGeneratorContext, step: LinearStep, references: LinearReferences)
fun CodeBlock.Builder.mapEntries(entries: Iterable<Map.Entry<Any?, TypeFactory>>, value: Type) = apply {
    for ((index, entry) in entries.withIndex()) {
        if (index > 0) addStatement(",")
        add("%L to ", mapEntryKey(entry.key))
        addCastArgument(index, value)
    }
    addStatement("")
}
