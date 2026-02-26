package spike.compiler.graph

@Suppress("RemoveRedundantQualifierName")
object BuiltInTypes {

    val Provider = Type(spike.Provider::class)
    val Lazy = Type(kotlin.Lazy::class)
    val Any = Type(kotlin.Any::class)
    val Map = Type(kotlin.collections.Map::class)
    val List = Type(kotlin.collections.List::class)
    val Set = Type(kotlin.collections.Set::class)

}

