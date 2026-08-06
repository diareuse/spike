package spike.compiler.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import spike.compiler.graph.GraphEntryPoint
import spike.compiler.graph.Parameter
import spike.compiler.graph.TypeFactory
import spike.compiler.graph.TypeFactory.Companion.dependencyTree
import spike.compiler.graph.TypeFactory.Companion.invertDependencyTree
import spike.factory.DependencyId

class ExportGenerator : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val graph = context.graph
        val resolver = context.resolver
        val ep = graph.entry
        val epcn = ClassName("spike.generated.${graph.entry.type.packageName}", graph.entry.type.simpleName + "Impl")
        val dfcn = context.dependencyFactoryClassName
        val params = graph.importFactories
        val type = TypeSpec.classBuilder(epcn)
            .addOriginatingFiles(context.originatingFiles)
            .addSuperinterface(resolver.getTypeName(ep.type))
            .addProperty(
                PropertySpec.builder("factory", dfcn)
                    .initializer(CodeBlock.builder()
                        .add("%T(", dfcn)
                        .apply {
                            for((index, param) in params.withIndex()) {
                                if(index > 0)
                                    add(", ")
                                add("_%L = %L", param.name, param.name)
                            }
                        }
                        .add(")")
                        .build())
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
        type.primaryConstructor(FunSpec.constructorBuilder()
            .addParameters(params.map {
                val type = (resolver.builtInType { Provider } as ClassName).parameterizedBy(it.type.toClassName())
                ParameterSpec.builder(it.name, type).build()
            }.toList())
            .build())
        generateMethods(ep, type, resolver, context)
        generateProperties(ep, type, resolver, context)
        val file = FileSpec.builder(epcn)
            .addType(type.build())
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                    .addMember(
                        "%S, %S",
                        "ClassName",
                        "RedundantVisibilityModifier"
                    )
                    .build()
            )
        collector.emit(file.build())
    }

    private fun generateMethods(
        ep: GraphEntryPoint,
        type: TypeSpec.Builder,
        resolver: TypeResolver,
        context: FileGeneratorContext
    ) {
        for (m in ep.methods) {
            type.addFunction(
                FunSpec.builder(m.name)
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameters(m.parameters.map { it.toParameterSpec() })
                    .returns(resolver.getTypeName(m.returns))
                    .addStatement(
                        "return factory.get(%L(%L))",
                        DependencyId::class.asClassName(),
                        context.getDependencyId(context.ids.find(m.returns))
                    )
                    .build()
            )
        }
    }

    private fun generateProperties(
        ep: GraphEntryPoint,
        type: TypeSpec.Builder,
        resolver: TypeResolver,
        context: FileGeneratorContext
    ) {
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
    }

    private fun Parameter.toParameterSpec() = ParameterSpec.builder(name, type.toTypeName())
        .build()
}
