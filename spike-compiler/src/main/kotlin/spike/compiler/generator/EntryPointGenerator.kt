package spike.compiler.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.graph.GraphEntryPoint
import spike.Include

@Include
class EntryPointGenerator : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val graph = context.graph
        val resolver = context.resolver
        val ep = graph.entry
        val epcn = resolver.peerClass(graph, "EntryPoint")
        val type = TypeSpec.objectBuilder(epcn)
            .addOriginatingFiles(context.originatingFiles)
            .addModifiers(KModifier.PRIVATE)
            .addType(createFactory(context, context.dependencyFactoryClassName))
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

    private fun createFactoryMethod(
        ep: GraphEntryPoint,
        file: FileSpec.Builder,
        context: FileGeneratorContext,
        resolver: TypeResolver,
        epcn: ClassName
    ) {
        if (ep.factory.method.parameters.isNotEmpty()) {
            file.addFunction(
                FunSpec.builder("factory")
                    .returns(context.resolver.getTypeName(ep.factory.type))
                    .receiver((resolver.getTypeName(ep.type) as ClassName).nestedClass("Companion"))
                    .addCode("return %T.Factory", epcn)
                    .build()
            )
        }
    }

    private fun createFactory(context: FileGeneratorContext, dfcn: ClassName): TypeSpec {
        val entryType = context.resolver.getTypeName(context.graph.entry.type)
        if (context.graph.entry.factory.method.parameters.isEmpty()) return TypeSpec.objectBuilder("Factory")
            .addFunction(
                FunSpec.builder("create")
                    .returns(entryType)
                    .addCode("return %T()", dfcn)
                    .build()
            )
            .build()
        val m = context.graph.entry.factory.method
        val ownership = EntryPointSingletonOwnership.of(context.graph.entry.factory)
        val arguments = ownership.dependencyFactoryArguments(m.parameters.map { it.name })
        return TypeSpec.objectBuilder("Factory")
            .addSuperinterface(context.resolver.getTypeName(context.graph.entry.factory.type))
            .apply(ownership::addMembers)
            .addFunction(
                FunSpec.builder(m.name)
                    .returns(context.resolver.getTypeName(m.returns))
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameters(m.parameters.map {
                        ParameterSpec.builder(it.name, context.resolver.getTypeName(it.type)).build()
                    })
                    .addCode("return %T(${arguments.joinToString()})", dfcn)
                    .build()
            )
            .build()
    }

    companion object {
        @Include
        @AppGraph
        val EntryPointGenerator.asGenerator: Generator
            get() = this
    }
}
