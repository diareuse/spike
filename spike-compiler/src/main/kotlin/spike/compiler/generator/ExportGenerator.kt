package spike.compiler.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import spike.compiler.graph.GraphEntryPoint
import spike.factory.DependencyId

class ExportGenerator(
    private val dependencyFactoryClassName: ClassName
) : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val graph = context.graph
        val resolver = context.resolver
        val ep = graph.entry
        val epcn = ClassName("spike.generated.${graph.entry.type.packageName}", graph.entry.type.simpleName + "Impl")
        val dfcn = dependencyFactoryClassName
        val type = TypeSpec.objectBuilder(epcn)
            .addOriginatingFiles(context.originatingFiles)
            .addSuperinterface(resolver.getTypeName(ep.type))
            .addProperty(
                PropertySpec.builder("factory", dfcn)
                    .initializer("%T()", dfcn)
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
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
}
