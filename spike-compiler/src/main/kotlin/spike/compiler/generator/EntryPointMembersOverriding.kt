package spike.compiler.generator

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import spike.factory.DependencyId
import spike.Include

@Include
class EntryPointMembersOverriding : EntryPointMembers {
    override fun addTo(factory: TypeSpec.Builder, context: FileGeneratorContext) {
        val entry = context.graph.entry
        val resolver = context.resolver
        factory.addSuperinterface(resolver.getTypeName(entry.type))
        for (property in entry.properties) {
            factory.addProperty(
                PropertySpec.builder(property.name, resolver.getTypeName(property.returns))
                    .apply { if (!property.synthetic) addModifiers(KModifier.OVERRIDE) }
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement(
                                "return get(%T(%L))",
                                DependencyId::class,
                                context.getDependencyId(context.graph.factoryOf(property))
                            )
                            .build()
                    )
                    .build()
            )
        }
        for (method in entry.methods) {
            factory.addFunction(
                FunSpec.builder(method.name)
                    .addModifiers(KModifier.OVERRIDE)
                    .returns(resolver.getTypeName(method.returns))
                    .addStatement(
                        "return get(%T(%L))",
                        DependencyId::class,
                        context.getDependencyId(context.graph.factoryOf(method))
                    )
                    .build()
            )
        }
    }

    companion object {
        @Include
        @AppGraph
        val EntryPointMembersOverriding.asMembers: EntryPointMembers
            get() = this
    }
}
