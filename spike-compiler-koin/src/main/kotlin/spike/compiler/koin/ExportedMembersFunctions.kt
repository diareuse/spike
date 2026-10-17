package spike.compiler.koin

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toTypeName
import spike.compiler.conversion.QualifierLookup
import spike.Include
import spike.BindTarget
import spike.compiler.conversion.Order

@Include
class ExportedMembersFunctions(private val qualifiers: QualifierLookup) : ExportedMembers {
    override fun of(entryPoint: KSClassDeclaration) = entryPoint.getAllFunctions()
        .filter { it.isAbstract && it.parameters.isEmpty() && qualifiers.unconvertible(it) == null }
        .map { ExportedMember(it.returnType!!.toTypeName(), it.simpleName.asString() + "()") }
        .toList()

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(20)
        val ExportedMembersFunctions.asMembers: ExportedMembers
            get() = this
    }
}
