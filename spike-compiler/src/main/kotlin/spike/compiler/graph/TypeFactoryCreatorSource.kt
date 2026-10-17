package spike.compiler.graph

/**
 * Builds a creator that every graph resolves through, for one graph. Applied in ascending `@Order`.
 */
fun interface TypeFactoryCreatorSource {
    fun of(state: GraphState): TypeFactoryCreator
}

/**
 * Builds a creator that only app graphs resolve through, for one graph. Applied in ascending `@Order`, after
 * every [TypeFactoryCreatorSource].
 */
fun interface AppCreatorSource {
    fun of(state: GraphState): TypeFactoryCreator
}
