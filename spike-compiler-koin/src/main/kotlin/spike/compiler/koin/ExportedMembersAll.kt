package spike.compiler.koin

import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include
import spike.compiler.conversion.ordered

@Include
class ExportedMembersAll(members: Map<Int, ExportedMembers>) : ExportedMembers {
    private val members = members.ordered()

    override fun of(entryPoint: KSClassDeclaration) = members.flatMap { it.of(entryPoint) }
}
