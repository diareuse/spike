package spike.compiler.generator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ksp.writeTo
import spike.graph.*
import java.util.Locale.getDefault

fun SymbolProcessorEnvironment.generateDependencyContainer(graph: DependencyGraph): DependencyContainer {
    val className = graph.entry.type.toClassName().peerClass { "DependencyContainer" }
    val type = TypeSpec.classBuilder(className)
        .addModifiers(KModifier.INTERNAL)
    val typeLut = mutableMapOf<Type, String>()
    fun getPropName(type: Type) =
        typeLut.getOrPut(type) { type.toClassName().simpleName.replaceFirstChar { it.lowercase(getDefault()) } }
    for (factory in graph) {
        val propertyName = getPropName(factory.type)
        val prop = PropertySpec.builder(propertyName, factory.type.toClassName())
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
                            factory.type.toClassName()
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

            is TypeFactory.Provides -> {
                prop.getter(FunSpec.getterBuilder().addStatement("%M()", MemberName("kotlin", "TODO")).build())
            }
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
    block.add("%T(", factory.type.toClassName())
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

fun Member.Method.toMemberName() = when (val p = parent) {
    null -> MemberName(packageName, name)
    else -> MemberName(p.toClassName(), name)
}

class DependencyContainer(
    val className: ClassName,
    val lut: Map<Type, String>
) {

    operator fun get(type: Type): String {
        return lut[type]!!
    }

}