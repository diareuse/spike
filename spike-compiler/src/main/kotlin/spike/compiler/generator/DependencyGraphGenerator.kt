@file:Suppress("DEPRECATION")

package spike.compiler.generator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import spike.compiler.generator.container.DependencyContainerTypeConstructor
import spike.compiler.generator.container.DependencyContainerTypeFactory
import spike.compiler.generator.container.DependencyContainerTypeInternal
import spike.compiler.generator.entrypoint.EntryPointFileFactoryAccessor
import spike.compiler.generator.entrypoint.EntryPointFileInitializer
import spike.compiler.generator.entrypoint.EntryPointTypeConstructor
import spike.compiler.generator.entrypoint.EntryPointTypeFunctions
import spike.compiler.generator.entrypoint.EntryPointTypeInternal
import spike.compiler.generator.entrypoint.EntryPointTypeProperties
import spike.compiler.generator.entrypoint.EntryPointTypeSuperinterface
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeInternal
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeMethod
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeSuperinterface
import spike.compiler.graph.DependencyGraph

class DependencyGraphGenerator(
    private val environment: SymbolProcessorEnvironment,
) {

    private val resolver = TypeResolver()

    fun generate(graph: DependencyGraph) {
        val dependencyContainerType = GeneratorChain.classType(
            subject = graph,
            resolver = resolver,
            name = resolver.getDependencyContainerClassName(graph.entry.type),
            DependencyContainerTypeConstructor(),
            DependencyContainerTypeFactory(),
            DependencyContainerTypeInternal(),
        )
        val dependencyContainerFile = GeneratorChain.file(
            subject = graph,
            resolver = resolver,
            name = resolver.getDependencyContainerClassName(graph.entry.type),
            type = dependencyContainerType,
        )
        try {
            dependencyContainerFile.proceed().build().writeTo(environment.codeGenerator, false)
        } catch (_: FileAlreadyExistsException) {
        }

        val entryPointFactoryType = GeneratorChain.objectType(
            subject = graph.entry.factory,
            resolver = resolver,
            name = resolver.transformClassName(graph.entry.factory.type),
            EntryPointFactoryTypeSuperinterface(),
            EntryPointFactoryTypeInternal(),
            EntryPointFactoryTypeMethod(),
        )
        val entryPointFactoryFile = GeneratorChain.file(
            subject = graph.entry.factory,
            resolver = resolver,
            name = resolver.transformClassName(graph.entry.factory.type),
            type = entryPointFactoryType,
        )
        try {
            entryPointFactoryFile.proceed().build().writeTo(environment.codeGenerator, false)
        } catch (_: FileAlreadyExistsException) {
        }

        val entryPointType = GeneratorChain.classType(
            subject = graph.entry,
            resolver = resolver,
            name = resolver.transformClassName(graph.entry.type),
            EntryPointTypeInternal(),
            EntryPointTypeSuperinterface(),
            EntryPointTypeConstructor(),
            EntryPointTypeProperties(),
            EntryPointTypeFunctions(),
        )
        val entryPointFile = GeneratorChain.file(
            subject = graph.entry,
            resolver = resolver,
            name = resolver.transformClassName(graph.entry.type),
            type = entryPointType,
            EntryPointFileFactoryAccessor(),
            EntryPointFileInitializer(),
        )
        try {
            entryPointFile.proceed().build().writeTo(environment.codeGenerator, false)
        } catch (_: FileAlreadyExistsException) {
        }
        val file = FileSpec.builder("test", "Generated")
        MegaGenerator(graph, resolver, environment.logger).generate().forEach {
            file.addType(it)
        }
        try {
            file.build().writeTo(environment.codeGenerator, false)
        } catch (_: FileAlreadyExistsException) {
        }
    }
}
