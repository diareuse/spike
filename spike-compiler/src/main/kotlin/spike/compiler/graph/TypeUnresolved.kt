package spike.compiler.graph

val Type.unresolvedNames: List<String>
    get() = when (this) {
        is Type.Simple -> listOfNotNull(errorTypeName(simpleName))
        is Type.Inner -> parent.unresolvedNames + listOfNotNull(errorTypeName(simpleName))
        is Type.Parametrized -> envelope.unresolvedNames + typeArguments.flatMap { it.unresolvedNames }
        is Type.WithVariance -> type?.unresolvedNames.orEmpty()
        is Type.Qualified -> type.unresolvedNames + qualifiers.flatMap { it.type.unresolvedNames }
    }

fun errorTypeName(name: String): String? =
    name.takeIf { it.startsWith(ErrorMarker) && it.endsWith(">") }?.removeSurrounding(ErrorMarker, ">")

private const val ErrorMarker = "<ERROR TYPE: "
