package spike.compiler.generator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.writeTo
import spike.graph.DependencyGraph
import spike.graph.Type

fun SymbolProcessorEnvironment.generateEntryPoint(graph: DependencyGraph) {
    val ep = graph.entry
    val className = ep.type.toClassName().peerClass { "Spike${it.simpleName}EntryPoint" }
    val container = generateDependencyContainer(graph)
    val constructor = FunSpec.constructorBuilder()
        .addParameter("container", container.className)
    val containerProperty = PropertySpec.builder("container", container.className)
        .addModifiers(KModifier.PRIVATE)
        .initializer("container")
    val type = TypeSpec.classBuilder(className)
        .primaryConstructor(constructor.build())
        .addModifiers(KModifier.PRIVATE)
        .addSuperinterface(ep.type.toClassName())
        .addProperty(containerProperty.build())
    for (prop in ep.properties) {
        val getter = FunSpec.getterBuilder()
            .addStatement("return container.%N", container[prop.returns])
        PropertySpec.builder(prop.name, prop.returns.toClassName())
            .getter(getter.build())
            .addModifiers(KModifier.OVERRIDE)
            .build()
            .also(type::addProperty)
    }
    for (fn in ep.methods) {
        FunSpec.builder(fn.name)
            .returns(fn.returns.toClassName())
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("return container.%N", container[fn.returns])
            .build()
            .also(type::addFunction)
    }
    val initializer = FunSpec.builder("invoke")
        .addModifiers(KModifier.OPERATOR)
        .receiver(ep.type.toClassName().nestedClass("Companion"))
        .returns(ep.type.toClassName())
        .addStatement("return %T(%T())", className, container.className)
    FileSpec.builder(className)
        .addType(type.build())
        .addFunction(initializer.build())
        .build()
        .writeTo(codeGenerator, false)
}

fun Type.toClassName(): ClassName = when (this) {
    is Type.Parametrized -> envelope.toClassName()
    is Type.Qualified -> type.toClassName()
    is Type.Simple -> ClassName(packageName, simpleName)
}

inline fun ClassName.peerClass(body: (ClassName) -> String) = peerClass(body(this))