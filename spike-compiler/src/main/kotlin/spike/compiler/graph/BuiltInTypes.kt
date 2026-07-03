package spike.compiler.graph

@Suppress("RemoveRedundantQualifierName")
object BuiltInTypes {

    val Provider = Type(spike.Provider::class, false)
    val Lazy = Type(kotlin.Lazy::class, false)
    val Any = Type(kotlin.Any::class, false)
    val Map = Type(kotlin.collections.Map::class, false)
    val List = Type(kotlin.collections.List::class, false)
    val Set = Type(kotlin.collections.Set::class, false)
}
