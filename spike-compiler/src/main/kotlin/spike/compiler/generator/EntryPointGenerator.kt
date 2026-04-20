package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import spike.factory.DependencyId

class EntryPointGenerator(
    private val dependencyFactoryClassName: ClassName
) : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val graph = context.graph
        val resolver = context.resolver
        val ep = graph.entry
        val epcn = resolver.peerClass(graph, "EntryPoint")
        val dfcn = dependencyFactoryClassName
        val type = TypeSpec.classBuilder(epcn)
            .addSuperinterface(resolver.getTypeName(ep.type))
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("factory", dfcn)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("factory", dfcn)
                    .initializer("factory")
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
            .addModifiers(KModifier.PRIVATE)
        type.addFunction(
            FunSpec.constructorBuilder()
                .addParameters(ep.factory.method.parameters.map {
                    ParameterSpec.builder(it.name, resolver.getTypeName(it.type)).build()
                })
                .callThisConstructor(CodeBlock.of("%T(${ep.factory.method.parameters.joinToString { it.name }})", dfcn))
                .build()
        )
        for (m in ep.methods) {
            type.addFunction(
                FunSpec.builder(m.name)
                    .addModifiers(KModifier.OVERRIDE)
                    .returns(resolver.getTypeName(m.returns))
                    .addStatement(
                        "return factory.get(%L(%L))",
                        DependencyId::class.asClassName(),
                        context.getDependencyId(context.ids.find(m.returns))
                    )
                    .build()
            )
        }
        for (p in ep.properties) {
            type.addProperty(
                PropertySpec.builder(p.name, resolver.getTypeName(p.returns))
                    .addModifiers(KModifier.OVERRIDE)
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement(
                                "return factory.get(%L(%L))",
                                DependencyId::class.asClassName(),
                                context.getDependencyId(context.ids.find(p.returns))
                            )
                            .build()
                    )
                    .build()
            )
        }
        val file = FileSpec.builder(epcn)
            .addType(type.build())
            .addFunction(
                FunSpec.builder("invoke")
                    .addModifiers(KModifier.OPERATOR)
                    .receiver((resolver.getTypeName(ep.type) as ClassName).nestedClass("Companion"))
                    .returns(resolver.getTypeName(ep.type))
                    .addParameters(ep.factory.method.parameters.map {
                        ParameterSpec.builder(it.name, resolver.getTypeName(it.type)).build()
                    })
                    .addStatement("return %T(${ep.factory.method.parameters.joinToString { it.name }})", epcn)
                    .build()
            )
            .build()
        collector.emit(file)
    }
}
