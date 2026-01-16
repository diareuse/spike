package spike.compiler.generator

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import spike.graph.DependencyGraph
import spike.graph.TypeFactory

class DependencyContainerFactory : TypeGenerator<DependencyGraph> {
    override fun generate(chain: TypeGeneratorChain<DependencyGraph>): TypeSpec.Builder {
        for (factory in chain.subject) {
            // Properties are a type of factory statically declared via constructor; i.e., parameter
            if (factory is TypeFactory.Property) continue

            // Unwrap providers to raw factories
            var factory = factory
            while (factory is TypeFactory.Provides)
                factory = factory.factory

            val propertyName = chain.resolver.getFieldName(factory.type)
            val propertyTypeName = chain.resolver.getTypeName(factory.type)
            val propertySpec = PropertySpec.Companion.builder(propertyName, propertyTypeName)

            // We don't need to expose internal properties
            if (!chain.subject.entry.isRootProperty(factory.type))
                propertySpec.addModifiers(KModifier.PRIVATE)

            when (factory) {
                is TypeFactory.Binds -> propertySpec.binds(factory, chain.resolver)
                is TypeFactory.Class -> propertySpec.classFactory(factory, chain.resolver)
                is TypeFactory.Method -> propertySpec.methodFactory(factory, chain.resolver)
                is TypeFactory.Property,
                is TypeFactory.Provides -> error("Compiler error, this should never be called")
            }

            chain.spec.addProperty(propertySpec.build())
        }
        return chain.proceed()
    }

    private fun PropertySpec.Builder.binds(factory: TypeFactory.Binds, resolver: TypeResolver) {
        val spec = FunSpec.Companion.getterBuilder()
            .addModifiers(KModifier.INLINE)
            .addStatement(
                "return %N as %T",
                resolver.getFieldName(factory.source.type),
                resolver.getTypeName(factory.type)
            )
            .build()
        getter(spec)
    }

    private fun PropertySpec.Builder.classFactory(factory: TypeFactory.Class, resolver: TypeResolver) {
        val chain = InvocationChain(
            subject = factory,
            generators = listOf(
                InvocationGeneratorCaller(),
                InvocationGeneratorConstructor(),
                InvocationGeneratorParameters()
            ),
            resolver = resolver
        )
        val codeBlock = chain.proceed().build()
        when (factory.singleton) {
            true -> delegate(codeBlock)
            else -> getter(FunSpec.Companion.getterBuilder().addModifiers(KModifier.INLINE).addCode(codeBlock).build())
        }
    }

    private fun PropertySpec.Builder.methodFactory(factory: TypeFactory.Method, resolver: TypeResolver) {
        val chain = InvocationChain(
            subject = factory,
            generators = listOf(
                InvocationGeneratorCaller(),
                InvocationGeneratorMethod(),
                InvocationGeneratorParameters()
            ),
            resolver = resolver
        )
        val codeBlock = chain.proceed().build()
        when (factory.singleton) {
            true -> delegate(codeBlock)
            else -> getter(FunSpec.Companion.getterBuilder().addModifiers(KModifier.INLINE).addCode(codeBlock).build())
        }
    }
}