package spike.compiler.generator

import com.squareup.kotlinpoet.TypeSpec

class EntryPointMembersNone : EntryPointMembers {
    override fun addTo(factory: TypeSpec.Builder, context: FileGeneratorContext) = Unit
}
