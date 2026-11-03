package spike.compiler.processor.symbol

import spike.Include

fun interface UnresolvedTypes {
    fun of(types: Sequence<SignatureType>): Sequence<UnresolvedReference>

    companion object {
        @Include
        val UnresolvedTypesError.unresolved: UnresolvedTypes
            get() = this
    }
}
