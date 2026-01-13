package spike.compiler.generator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo
import spike.graph.DependencyGraph
import spike.graph.GraphEntryPoint
import spike.graph.Type

fun SymbolProcessorEnvironment.generateEntryPoint(graph: DependencyGraph) {
    val ep = graph.entry
    val className = ep.type.toClassName().peerClass { "Spike${it.simpleNames.joinToString("")}EntryPoint" }.asRootClass()
    val container = generateDependencyContainer(graph)
    val constructor = FunSpec.constructorBuilder()
        .addParameter("container", container.className)
    val containerProperty = PropertySpec.builder("container", container.className)
        .addModifiers(KModifier.PRIVATE)
        .initializer("container")
    val type = TypeSpec.classBuilder(className)
        .primaryConstructor(constructor.build())
        .addModifiers(KModifier.INTERNAL)
        .addSuperinterface(ep.type.toTypeName())
        .addProperty(containerProperty.build())
    for (prop in ep.properties) {
        val getter = FunSpec.getterBuilder()
            .addStatement("return container.%N", container[prop.returns])
        PropertySpec.builder(prop.name, prop.returns.toTypeName())
            .getter(getter.build())
            .addModifiers(KModifier.OVERRIDE)
            .build()
            .also(type::addProperty)
    }
    for (fn in ep.methods) {
        FunSpec.builder(fn.name)
            .returns(fn.returns.toTypeName())
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("return container.%N", container[fn.returns])
            .build()
            .also(type::addFunction)
    }
    val initializer = FunSpec.builder("invoke")
        .addModifiers(KModifier.OPERATOR)
        .receiver(ep.type.toClassName().nestedClass("Companion"))
        .returns(ep.type.toTypeName())
    val initializerBody = CodeBlock.builder()
    val epFactory = ep.factory
    if (epFactory != null) {
        initializerBody.add(
            "return %T().%N(",
            generateEntryPointFactory(epFactory, className, container.className),
            epFactory.method.name
        )
        for ((index, p) in epFactory.method.parameters.withIndex()) {
            if (index > 0) initializerBody.add(", ")
            initializerBody.add("%N", p.name)
            initializer.addParameter(p.name, p.type.toTypeName())
        }
        initializerBody.add(")")
    } else {
        initializerBody.add("return %T(%T())", className, container.className)
    }
    initializer.addCode(initializerBody.build())
    FileSpec.builder(className)
        .addType(type.build())
        .addFunction(initializer.build())
        .build()
        .writeTo(codeGenerator, false)
}

fun SymbolProcessorEnvironment.generateEntryPointFactory(
    factory: GraphEntryPoint.Factory,
    entryPointClassName: ClassName,
    containerClassName: ClassName
): ClassName {
    val className = factory.type.toClassName().peerClass { "Spike${it.simpleNames.joinToString("")}" }.asRootClass()
    val type = TypeSpec.classBuilder(className)
        .addSuperinterface(factory.type.toTypeName())
        .addModifiers(KModifier.INTERNAL)
    val factoryMethod = FunSpec.builder(factory.method.name)
        .addModifiers(KModifier.OVERRIDE)
        .returns(factory.method.returns.toTypeName())
    val cb = CodeBlock.builder()
        .add("return %T(%T(", entryPointClassName, containerClassName)
    for ((index, p) in factory.method.parameters.withIndex()) {
        factoryMethod.addParameter(p.name, p.type.toTypeName())
        if (index > 0) cb.add(", ")
        cb.add("%N", p.name)
    }
    cb.add("))")
    type.addFunction(factoryMethod.addCode(cb.build()).build())
    FileSpec.builder(className)
        .addType(type.build())
        .build()
        .writeTo(codeGenerator, false)
    return className
}

fun Type.toClassName(): ClassName = when (this) {
    is Type.Parametrized -> envelope.toClassName()
    is Type.Qualified -> type.toClassName()
    is Type.Simple -> ClassName(packageName, simpleName)
    is Type.Inner -> ClassName(packageName, names)
}

fun Type.toTypeName(): TypeName = when (this) {
    is Type.Parametrized -> envelope.toClassName().parameterizedBy(typeArguments.map { it.toTypeName() })
    is Type.Qualified -> type.toClassName()
    is Type.Simple -> ClassName(packageName, simpleName)
    is Type.Inner -> ClassName(packageName, names)
}

inline fun ClassName.peerClass(body: (ClassName) -> String) = peerClass(body(this))
fun ClassName.asRootClass() = ClassName(packageName, simpleName)