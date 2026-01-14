package spike.compiler.generator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ksp.writeTo
import spike.graph.*
import spike.graph.TypeFactory.Companion.contains
import java.util.Locale.getDefault

fun SymbolProcessorEnvironment.generateDependencyContainer(graph: DependencyGraph): DependencyContainer {
    val className = graph.entry.type.toClassName().peerClass { "DependencyContainer" }
    val type = TypeSpec.classBuilder(className)
        .addModifiers(KModifier.INTERNAL)
    val typeLut = mutableMapOf<Type, String>()
    fun getPropName(type: Type) =
        typeLut.getOrPut(type) { type.descriptor.replaceFirstChar { it.lowercase(getDefault()) } }

    val constructor = FunSpec.constructorBuilder().apply {
        for (p in graph.entry.factory?.method?.parameters.orEmpty()) {
            addParameter(p.name, p.type.toTypeName())
            val name = getPropName(p.type)
            val ps = PropertySpec
                .builder(name, p.type.toTypeName()).initializer(p.name)
                .addModifiers(if (p.type in graph.properties) KModifier.PUBLIC else KModifier.PRIVATE)
            type.addProperty(ps.build())
        }
    }
    type.primaryConstructor(constructor.build())
    for (factory in graph) {
        if (factory is TypeFactory.Property) continue
        var factory = factory
        while (factory is TypeFactory.Provides)
            factory = factory.factory
        val propertyName = getPropName(factory.type)
        val prop = PropertySpec.builder(propertyName, factory.type.toTypeName())
        if (!graph.entry.isRootProperty(factory.type))
            prop.addModifiers(KModifier.PRIVATE)
        when (factory) {
            is TypeFactory.Binds -> {
                prop.getter(
                    FunSpec.getterBuilder()
                        .addModifiers(KModifier.INLINE)
                        .addStatement(
                            "return %N as %T",
                            className.member(getPropName(factory.source.type)),
                            factory.type.toTypeName()
                        )
                        .build()
                )
            }

            is TypeFactory.Class -> when (factory.singleton) {
                true -> prop.delegate(generateInvocation(factory, ::getPropName))
                else -> prop.getter(
                    FunSpec.getterBuilder().addModifiers(KModifier.INLINE)
                        .addCode(generateInvocation(factory, ::getPropName)).build()
                )
            }

            is TypeFactory.Method -> when (factory.singleton) {
                true -> prop.delegate("%M { %M() }", MemberName("kotlin", "lazy"), factory.member.toMemberName())
                else -> prop.getter(
                    FunSpec.getterBuilder().addModifiers(KModifier.INLINE)
                        .addStatement("return %M()", factory.member.toMemberName()).build()
                )
            }

            is TypeFactory.Provides,
            is TypeFactory.Property -> error("This is a compiler error, this will never be called")
        }

        prop
            .build()
            .also(type::addProperty)
    }
    FileSpec.builder(className)
        .addType(type.build())
        .build()
        .writeTo(codeGenerator, false)
    return DependencyContainer(
        className,
        typeLut
    )
}

fun generateInvocation(factory: TypeFactory.Class, lut: (Type) -> String): CodeBlock {
    val block = CodeBlock.builder()
    if (factory.singleton) block.beginControlFlow("%M {", MemberName("kotlin", "lazy"))
    else block.add("return ")
    block.add("%T(", factory.type.toTypeName())
    var added = false
    for (param in factory.invocation.parameters) {
        if (added) block.add(", ")
        else added = true
        block.add("%N = ", param.name)
        var t = param.type
        // use reference to field as a provider, which should be compiler-optimized to avoid allocation
        if (t is Type.Parametrized && t.envelope == ProviderType) {
            block.add("::")
            t = t.typeArguments.single()
        }
        if (t is Type.Parametrized && t.envelope == LazyType)
            block.add("%M(::%N)", MemberName("kotlin", "lazy"), lut(t.typeArguments.single()))
        else
            block.add("%N", lut(t))
    }
    block.add(")")
    if (factory.singleton) {
        block.add("\n")
        block.endControlFlow()
    }
    return block.build()
}
