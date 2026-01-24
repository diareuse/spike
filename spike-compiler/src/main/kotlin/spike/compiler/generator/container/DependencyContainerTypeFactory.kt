package spike.compiler.generator.container

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.generator.invocation.*
import spike.graph.DependencyGraph
import spike.graph.TypeFactory

class DependencyContainerTypeFactory : TypeGenerator<DependencyGraph> {
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
            val propertySpec = PropertySpec.builder(propertyName, propertyTypeName)
                .addModifiers(if (factory.isPublic) KModifier.PUBLIC else KModifier.PRIVATE)

            when (factory) {
                is TypeFactory.Binds -> propertySpec.binds(factory, chain.resolver)
                is TypeFactory.Class -> propertySpec.classFactory(factory, chain.resolver)
                is TypeFactory.Method -> propertySpec.methodFactory(factory, chain.resolver)
                is TypeFactory.Property,
                is TypeFactory.Provides -> error("Compiler error, this should never be called")
                is TypeFactory.MultibindsCollection -> TODO()
                is TypeFactory.MultibindsMap -> TODO()
            }

            chain.spec.addProperty(propertySpec.build())
        }
        return chain.proceed()
    }

    private fun PropertySpec.Builder.binds(factory: TypeFactory.Binds, resolver: TypeResolver) {
        val spec = FunSpec.getterBuilder()
            .addStatement(
                "return %N as %T",
                resolver.getFieldName(factory.source.type),
                resolver.getTypeName(factory.type)
            )
        if (factory.canInline)
            spec.addModifiers(KModifier.INLINE)
        getter(spec.build())
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
            else -> {
                val spec = FunSpec.getterBuilder().addCode(codeBlock)
                if (factory.canInline)
                    spec.addModifiers(KModifier.INLINE)
                getter(spec.build())
            }
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
            else -> {
                val spec = FunSpec.getterBuilder().addCode(codeBlock)
                if (factory.canInline)
                    spec.addModifiers(KModifier.INLINE)
                getter(spec.build())
            }
        }
    }
}