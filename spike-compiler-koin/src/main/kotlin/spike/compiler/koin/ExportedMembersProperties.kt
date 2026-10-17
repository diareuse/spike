package spike.compiler.koin

import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toTypeName
import spike.compiler.conversion.QualifierLookup
import spike.Include
import spike.BindTarget
import spike.compiler.conversion.Order

@Include
class ExportedMembersProperties(private val qualifiers: QualifierLookup) : ExportedMembers {
    override fun of(entryPoint: KSClassDeclaration) = entryPoint.getAllProperties()
        .filter { it.isAbstract() && it.extensionReceiver == null && qualifiers.unconvertible(it) == null }
        .map { ExportedMember(it.type.toTypeName(), it.simpleName.asString()) }
        .toList()

    companion object {
        @Include(bindTo = BindTarget.Map)
        @Order(10)
        val ExportedMembersProperties.asMembers: ExportedMembers
            get() = this
    }
}
