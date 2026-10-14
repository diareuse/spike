package spike.compiler.generator

import com.squareup.kotlinpoet.TypeSpec

fun interface EntryPointMembers {
    fun addTo(factory: TypeSpec.Builder, context: FileGeneratorContext)
}
