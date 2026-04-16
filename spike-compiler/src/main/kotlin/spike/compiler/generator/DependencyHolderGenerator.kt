package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import spike.compiler.graph.TypeFactory

class DependencyHolderGenerator(
    private val index: Int,
    private val dependencyFactoryClassName: ClassName
) : Generator {
    override fun generate(context: FileGeneratorContext, collector: FileSpecCollector) {
        val factories = context.ids.toList()[index]
        val className = context.resolver.peerClass(context.graph, "DependencyHolder${index}")
        val type = TypeSpec.objectBuilder(className)
        type.addFunction(context.run { createCreateMethod(factories) })
        val file = FileSpec.builder(className)
            .addType(type.build())
            .build()
        collector.emit(file)
    }

    context(context: FileGeneratorContext)
    private fun createCreateMethod(factories: List<TypeFactory>): FunSpec {
        val builder = FunSpec.builder("create")
            .addModifiers(KModifier.INTERNAL)
            .addParameter("buffer", Array::class.asTypeName().parameterizedBy(Any::class.asTypeName().copy(nullable = true)))
            .addParameter("position", Int::class)
            .returns(Any::class)

        val body = CodeBlock.builder()
        body.beginControlFlow("return when(position) {")
        // the index here expects a well-ordered factories argument
        for ((index, factory) in factories.withIndex()) context.apply {
            body.add("$index -> ")
            when (factory) {
                is TypeFactory.Binds -> body.addBufferCast(0, factory.type).addStatement("")
                is TypeFactory.Class -> body.addType(factory.type) {
                    body.addParameters(factory.invocation)
                }.addStatement("")
                is TypeFactory.Method -> body.addMember(factory.member) {
                    body.addParameters(factory.invocation)
                }.addStatement("")
                is TypeFactory.Memorizes -> body.addLazy {
                    addDependencyFactoryCall(dependencyFactoryClassName, factory.factory)
                }.addStatement("")
                is TypeFactory.Provides -> body.addProvider {
                    addDependencyFactoryCall(dependencyFactoryClassName, factory.factory)
                }.addStatement("")
                is TypeFactory.MultibindsCollection -> body.addMember(factory.collectionMemberFactory) {
                    addParameters(factory.entries)
                }.addStatement("")
                is TypeFactory.MultibindsMap -> body.addMap(factory.type.typeArguments[0], factory.type.typeArguments[1]) {
                    mapEntries(dependencyFactoryClassName, factory.keyValues.entries)
                }.addStatement("")
                is TypeFactory.Property -> error("Properties are unsupported, bind using external holder")
            }
        }
        body.addStatement("else -> error(\"Invalid position\")")
        body.endControlFlow()
        builder.addCode(body.build())

        return builder.build()
    }
}