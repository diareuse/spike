package spike.compiler.koin

import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include

/**
 * Finds the members of an entry point that a Koin module can expose.
 */
fun interface ExportedMembers {
    fun of(entryPoint: KSClassDeclaration): List<ExportedMember>

    companion object {
        @Include
        val ExportedMembersAll.asMembers: ExportedMembers
            get() = this
    }
}
