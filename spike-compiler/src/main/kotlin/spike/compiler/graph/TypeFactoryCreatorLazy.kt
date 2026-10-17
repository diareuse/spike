package spike.compiler.graph

class TypeFactoryCreatorLazy(private val recursions: Recursions) : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        if (type is Type.Parametrized && type.envelope == BuiltInTypes.Lazy) {
            val target = type.typeArguments.single()
            val dependency = if (context.isResolving(target)) recursions.reference(target) else mint(context, target)
            return TypeFactory.Memorizes(type, dependency)
        }
        return pass(context)
    }
}
