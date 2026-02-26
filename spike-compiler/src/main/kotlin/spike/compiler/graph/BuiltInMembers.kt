package spike.compiler.graph

object BuiltInMembers {

    val lazy = Member.Method(packageName = "kotlin", name = "lazy", returns = BuiltInTypes.Lazy, parent = null)
    val listOf = Member.Method(packageName = "kotlin.collections", name = "listOf", returns = BuiltInTypes.List, parent = null)
    val setOf = Member.Method(packageName = "kotlin.collections", name = "setOf", returns = BuiltInTypes.Set, parent = null)
    val mapOf = Member.Method(packageName = "kotlin.collections", name = "mapOf", returns = BuiltInTypes.Map, parent = null)

}