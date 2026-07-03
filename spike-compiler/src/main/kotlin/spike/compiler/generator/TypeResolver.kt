package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.TypeName
import spike.Include
import spike.Singleton
import spike.compiler.graph.BuiltInMembers
import spike.compiler.graph.BuiltInTypes
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.Member
import spike.compiler.graph.Type

@Singleton
@Include
class TypeResolver {

    inline fun builtInType(block: BuiltInTypes.() -> Type) = getTypeName(BuiltInTypes.block())
    inline fun builtInMember(block: BuiltInMembers.() -> Member) = getMemberName(BuiltInMembers.block())

    // ---

    fun peerClass(graph: DependencyGraph, name: String) = graph.entry.type.toClassName()
        .peerClass { it.simpleNames.joinToString("") + "_$name" }
        .asRootClass()

    // ---

    private val Type.descriptor: String
        get() = when (this) {
            is Type.Inner -> parent.descriptor + "In" + simpleName
            is Type.Parametrized -> envelope.descriptor + typeArguments.joinToString("And", "Of") {
                it.descriptor
            }
            is Type.Qualified -> qualifiers.joinToString("") {
                it.type.descriptor + it.arguments.joinToString {
                    it.name.replaceFirstChar { it.uppercase() } +
                        it.value.toString().replaceFirstChar { it.uppercase() }
                }
            } + type.descriptor
            is Type.Simple -> simpleName
            is Type.WithVariance -> variance.toString() + type?.descriptor?.replaceFirstChar { it.uppercase() }.orEmpty()
        }

    // ---

    private val typeName = mutableMapOf<Type, TypeName>()

    fun getTypeName(type: Type) = typeName.getOrPut(type) {
        type.toTypeName()
    }

    // ---

    private val memberName = mutableMapOf<Member, MemberName>()

    fun getMemberName(member: Member) = memberName.getOrPut(member) {
        member.toMemberName()
    }
}
