package spike.compiler.generator

import com.squareup.kotlinpoet.MemberName
import spike.graph.Member

fun Member.Method.toMemberName() = when (val p = parent) {
    null -> MemberName(packageName, name)
    else -> MemberName(p.toClassName(), name)
}
