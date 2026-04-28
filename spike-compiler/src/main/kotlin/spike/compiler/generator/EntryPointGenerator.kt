package spike.compiler.generator

import com.google.devtools.ksp.symbol.AnnotationUseSiteTarget
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import spike.compiler.graph.GraphEntryPoint
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
            .addOriginatingFiles(context.originatingFiles)
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
        type.addType(createFactory(context, epcn, dfcn))
        generateMethods(ep, type, resolver, context)
        generateProperties(ep, type, resolver, context)
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
                    .addStatement(
                        "return %T.Factory.create(${ep.factory.method.parameters.joinToString { it.name }})",
                        epcn
                    )
                    .build()
            )
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
        createFactoryMethod(ep, file, context, resolver, epcn)
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

    private fun createFactoryMethod(
        ep: GraphEntryPoint,
        file: FileSpec.Builder,
        context: FileGeneratorContext,
        resolver: TypeResolver,
        epcn: ClassName
    ) {
        if (!ep.factory.isVirtual) {
            file.addFunction(
                FunSpec.builder("factory")
                    .returns(context.resolver.getTypeName(ep.factory.type))
                    .receiver((resolver.getTypeName(ep.type) as ClassName).nestedClass("Companion"))
                    .addCode("return %T.Factory", epcn)
                    .build()
            )
        }
    }

    private fun createFactory(context: FileGeneratorContext, epcn: ClassName, dfcn: ClassName): TypeSpec {
        if (context.graph.entry.factory.isVirtual) return TypeSpec.objectBuilder("Factory")
            .addFunction(
                FunSpec.builder("create")
                    .returns(epcn)
                    .addCode("return %T(%T())", epcn, dfcn)
                    .build()
            )
            .build()
        val m = context.graph.entry.factory.method
        return TypeSpec.objectBuilder("Factory")
            .addSuperinterface(context.resolver.getTypeName(context.graph.entry.factory.type))
            .addFunction(
                FunSpec.builder(m.name)
                    .returns(context.resolver.getTypeName(m.returns))
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameters(m.parameters.map {
                        ParameterSpec.builder(it.name, context.resolver.getTypeName(it.type)).build()
                    })
                    .addCode("return %T(%T(${m.parameters.joinToString { it.name }}))", epcn, dfcn)
                    .build()
            )
            .build()
    }
}
