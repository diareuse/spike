package spike.compiler.generator

import com.squareup.kotlinpoet.MemberName
import spike.graph.Member

fun Member.toMemberName(): MemberName = when (this) {
    is Member.Method -> when (val p = parent) {
        null -> MemberName(packageName, name)
        else -> MemberName(p.toClassName(), name)
    }

    is Member.Property -> when (val p = parent) {
        null -> MemberName(packageName, name)
        else -> MemberName(p.toClassName(), name)
    }

    is Member.Receiver -> member.toMemberName()
}