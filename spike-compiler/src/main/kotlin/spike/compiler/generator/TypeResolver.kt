package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.TypeName
import spike.graph.BuiltInMembers
import spike.graph.BuiltInTypes
import spike.graph.Member
import spike.graph.Type

class TypeResolver {

    inline val builtInType get() = BuiltInTypes
    inline fun builtInType(block: BuiltInTypes.() -> Type) = getTypeName(BuiltInTypes.block())
    inline fun builtInMember(block: BuiltInMembers.() -> Member) = getMemberName(BuiltInMembers.block())

    // ---

    private val className = mutableMapOf<Type, ClassName>()

    fun transformClassName(type: Type) = className.getOrPut(type) {
        type.toClassName()
            .peerClass { "Spike${it.simpleNames.joinToString("")}" }
            .asRootClass()
    }

    fun getDependencyContainerClassName(entryPoint: Type) =
        transformClassName(Type.Simple(entryPoint.packageName, "DependencyContainer"))

    // ---

    private val fieldName = mutableMapOf<Type, String>()
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

    fun getFieldName(type: Type) = fieldName.getOrPut(type) {
        type.descriptor.replaceFirstChar { it.lowercase() }
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