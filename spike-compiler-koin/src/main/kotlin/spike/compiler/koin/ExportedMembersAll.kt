package spike.compiler.koin

import com.google.devtools.ksp.symbol.KSClassDeclaration

class ExportedMembersAll(private val members: List<ExportedMembers>) : ExportedMembers {
    override fun of(entryPoint: KSClassDeclaration) = members.flatMap { it.of(entryPoint) }
}
