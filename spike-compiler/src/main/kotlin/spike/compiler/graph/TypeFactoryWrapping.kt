package spike.compiler.graph

private val Wrappers: Map<Type, (Type.Parametrized, TypeFactory) -> TypeFactory> = mapOf(
    BuiltInTypes.Provider to TypeFactory::Provides,
    BuiltInTypes.Lazy to TypeFactory::Memorizes,
)

/**
 * Wraps a factory the way a requested Provider or Lazy of its type expects, and leaves it as is otherwise.
 */
internal fun TypeFactory.wrappedAs(type: Type): TypeFactory {
    val parametrized = type as? Type.Parametrized ?: return this
    return Wrappers[parametrized.envelope]?.invoke(parametrized, this) ?: this
}
