package spike.compiler.generator.container

import com.squareup.kotlinpoet.*
import spike.compiler.generator.TypeGenerator
import spike.compiler.generator.TypeGeneratorChain
import spike.compiler.generator.TypeResolver
import spike.compiler.generator.invocation.InvocationChain
import spike.compiler.generator.invocation.InvocationGeneratorConstructor
import spike.compiler.generator.invocation.InvocationGeneratorMethod
import spike.compiler.generator.invocation.InvocationGeneratorParameters
import spike.graph.DependencyGraph
import spike.graph.TypeFactory
import kotlin.reflect.KClass

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
                is TypeFactory.Class -> propertySpec.callableFactory(factory, chain.resolver)
                is TypeFactory.Method -> propertySpec.callableFactory(factory, chain.resolver)
                is TypeFactory.Property,
                is TypeFactory.Provides -> error("Compiler error, this should never be called")

                is TypeFactory.MultibindsCollection -> propertySpec.multibindsCollection(factory, chain.resolver)
                is TypeFactory.MultibindsMap -> propertySpec.multibindsMap(factory, chain.resolver)
            }

            chain.spec.addProperty(propertySpec.build())
        }
        return chain.proceed()
    }

    // ---

    private fun constructCallable(factory: TypeFactory.Callable, resolver: TypeResolver): CodeBlock {
        val chain = InvocationChain(
            subject = factory,
            generators = listOf(
                InvocationGeneratorConstructor(),
                InvocationGeneratorMethod(),
                InvocationGeneratorParameters()
            ),
            resolver = resolver
        )
        return chain.proceed().build()
    }

    // ---

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

    private fun PropertySpec.Builder.callableFactory(factory: TypeFactory.Callable, resolver: TypeResolver) {
        val codeBlock = CodeBlock.builder()
        when (factory.singleton) {
            true -> codeBlock.beginControlFlow("%M {", resolver.builtInMember { lazy })
            else -> codeBlock.add("return ")
        }
        codeBlock.add(constructCallable(factory, resolver))
        when (factory.singleton) {
            true -> {
                codeBlock.endControlFlow()
                delegate(codeBlock.build())
            }

            else -> {
                val spec = FunSpec.getterBuilder().addCode(codeBlock.build())
                if (factory.canInline)
                    spec.addModifiers(KModifier.INLINE)
                getter(spec.build())
            }
        }
    }

    private fun PropertySpec.Builder.multibindsCollection(
        factory: TypeFactory.MultibindsCollection,
        resolver: TypeResolver
    ) {
        val codeBlock = CodeBlock.builder()
        codeBlock.add("return %M(", resolver.getMemberName(factory.collectionMemberFactory))
        codeBlock.withIndent {
            for ((index, item) in factory.entries.withIndex()) {
                if (index > 0) codeBlock.add(", ")
                val callable = item.toCallableOrNull()
                    ?: TODO("Constructing $item is not implemented yet in multibinding context")
                codeBlock.add(constructCallable(callable, resolver))
            }
        }
        codeBlock.add(")")
        val spec = FunSpec.getterBuilder().addCode(codeBlock.build())
        if (factory.canInline)
            spec.addModifiers(KModifier.INLINE)
        getter(spec.build())
    }

    private fun PropertySpec.Builder.multibindsMap(factory: TypeFactory.MultibindsMap, resolver: TypeResolver) {
        val codeBlock = CodeBlock.builder()
        codeBlock.add("return %M(", resolver.builtInMember { mapOf })
        codeBlock.withIndent {
            var index = 0
            for ((key, item) in factory.keyValues) {
                if (index++ > 0) codeBlock.add(", ")
                val keyLiteral = when (key) {
                    is String -> "\"$key\""
                    is KClass<*> -> "${key.qualifiedName}::class"
                    else -> key
                }
                codeBlock.add("%L to ", keyLiteral)
                val callable = item.toCallableOrNull()
                    ?: TODO("Constructing $item is not implemented yet in multibinding context")
                codeBlock.add(constructCallable(callable, resolver))
            }
        }
        codeBlock.add(")")
        getter(FunSpec.getterBuilder().addCode(codeBlock.build()).build())
    }

    // ---

    private fun TypeFactory.toCallableOrNull(): TypeFactory.Callable? = when (this) {
        is TypeFactory.Callable -> this
        is TypeFactory.Binds -> source.toCallableOrNull()
        else -> null
    }
}