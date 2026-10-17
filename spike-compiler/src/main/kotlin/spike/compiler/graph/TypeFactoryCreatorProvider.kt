package spike.compiler.graph

class TypeFactoryCreatorProvider(private val recursions: Recursions) : TypeFactoryCreator {
    override suspend fun Minting.create(context: TypeFactoryCreator.Context): TypeFactory {
        val type = context.type
        if (type is Type.Parametrized && type.envelope == BuiltInTypes.Provider) {
            val target = type.typeArguments.single()
            val dependency = if (context.isResolving(target)) recursions.reference(target) else mint(context, target)
            return TypeFactory.Provides(type, dependency)
        }
        return pass(context)
    }
}
