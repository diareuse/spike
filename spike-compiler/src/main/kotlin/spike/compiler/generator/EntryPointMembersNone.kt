package spike.compiler.generator

import com.squareup.kotlinpoet.TypeSpec
import spike.Include

@Include
class EntryPointMembersNone : EntryPointMembers {
    override fun addTo(factory: TypeSpec.Builder, context: FileGeneratorContext) = Unit

    companion object {
        @Include
        @ExportGraph
        val EntryPointMembersNone.asMembers: EntryPointMembers
            get() = this
    }
}
