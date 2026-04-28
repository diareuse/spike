package spike.compiler.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import spike.compiler.generator.code.addBufferCast
import spike.compiler.generator.code.addDependencyFactoryCall
import spike.compiler.generator.code.addLazy
import spike.compiler.generator.code.addMap
import spike.compiler.generator.code.addMember
import spike.compiler.generator.code.addOptionalSingleton
import spike.compiler.generator.code.addParameters
import spike.compiler.generator.code.addProvider
import spike.compiler.generator.code.addType
import spike.compiler.generator.code.mapEntries
import spike.compiler.graph.TypeFactory
import spike.compiler.graph.TypeFactory.Binds
import spike.compiler.graph.TypeFactory.Class
import spike.compiler.graph.TypeFactory.Memorizes
import spike.compiler.graph.TypeFactory.Method
import spike.compiler.graph.TypeFactory.MultibindsCollection
import spike.compiler.graph.TypeFactory.MultibindsMap
import spike.compiler.graph.TypeFactory.Property
import spike.compiler.graph.TypeFactory.Provides
import spike.factory.SingletonHolder
import java.util.concurrent.atomic.AtomicInteger

class DependencyHolderGenerator(
    private val index: Int,
    private val dependencyFactoryClassName: ClassName
) : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val factories = context.ids.toList()[index]
        val className = context.resolver.peerClass(context.graph, "DependencyHolder${index}")
        val type = TypeSpec.classBuilder(className)
        type.primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("factory", dependencyFactoryClassName)
                .build()
        )
        type.addProperty(
            PropertySpec.builder("factory", dependencyFactoryClassName)
                .addModifiers(KModifier.PRIVATE)
                .initializer("factory")
                .build()
        )
        val singletonCounter = AtomicInteger()
        type.addFunction(createCreateMethod(context, factories, singletonCounter))
        if (singletonCounter.get() > 0) {
            val singletons = PropertySpec.builder("singletons", SingletonHolder::class, KModifier.PRIVATE)
                .initializer("%T()", SingletonHolder::class.asClassName())
                .build()
            val companion = TypeSpec.companionObjectBuilder()
                .addProperty(singletons)
                .build()
            type.addType(companion)
        }
        val file = FileSpec.builder(className)
            .addType(type.build())
            .addAnnotation(AnnotationSpec.builder(Suppress::class)
                .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                .addMember(
                    "%S, %S, %S, %S",
                    "UNCHECKED_CAST",
                    "unused",
                    "RedundantVisibilityModifier",
                    "ClassName"
                )
                .build())
            .build()
        collector.emit(file)
    }

    private fun createCreateMethod(
        context: FileGeneratorContext,
        factories: List<TypeFactory>,
        singletonCounter: AtomicInteger
    ): FunSpec {
        val builder = FunSpec.builder("create")
            .addModifiers(KModifier.INTERNAL)
            .addParameter("buffer", ArrayOfAny)
            .addParameter("position", Int::class)
            .returns(Any::class)

        val body = CodeBlock.builder()
        body.beginControlFlow("return when(position) {")
        // the index here expects a well-ordered factories argument
        for ((index, factory) in factories.withIndex()) context.apply {
            body.add("$index -> ")
            when (factory) {
                is Binds -> body.addBufferCast(0, factory.type).addStatement("")
                is Class -> body.addOptionalSingleton(index, factory.singleton) {
                    if (factory.singleton) singletonCounter.incrementAndGet()
                    addType(factory.type) {
                        body.addParameters(factory.invocation)
                    }.addStatement("")
                }
                is Method -> body.addOptionalSingleton(index, factory.singleton) {
                    if (factory.singleton) singletonCounter.incrementAndGet()
                    body.addMember(factory.member) {
                        addParameters(factory.invocation)
                    }.addStatement("")
                }
                is Memorizes -> body.addLazy {
                    addDependencyFactoryCall(factory.factory)
                }.addStatement("")
                is Provides -> body.addProvider {
                    addDependencyFactoryCall(factory.factory)
                }.addStatement("")
                is MultibindsCollection -> body.addMember(factory.collectionMemberFactory) {
                    addParameters(factory.entries)
                }.addStatement("")
                is MultibindsMap -> body.addMap(
                    key = factory.type.typeArguments[0],
                    value = factory.type.typeArguments[1]
                ) {
                    mapEntries(factory.keyValues.entries)
                }.addStatement("")
                is Property -> body.addStatement("factory.${factory.name}")
            }
        }
        body.addStatement("else -> error(\"Invalid position\")")
        body.endControlFlow()
        builder.addCode(body.build())

        return builder.build()
    }

    private companion object {
        val ArrayOfAny = Array::class.asClassName()
            .parameterizedBy(Any::class.asTypeName().copy(nullable = true))
    }
}
