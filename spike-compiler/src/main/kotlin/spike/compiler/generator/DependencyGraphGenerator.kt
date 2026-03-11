package spike.compiler.generator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import spike.compiler.generator.container.DependencyContainerFileWithType
import spike.compiler.generator.container.DependencyContainerTypeConstructor
import spike.compiler.generator.container.DependencyContainerTypeFactory
import spike.compiler.generator.container.DependencyContainerTypeInternal
import spike.compiler.generator.entrypoint.EntryPointFileFactoryAccessor
import spike.compiler.generator.entrypoint.EntryPointFileInitializer
import spike.compiler.generator.entrypoint.EntryPointFileWithType
import spike.compiler.generator.entrypoint.EntryPointTypeConstructor
import spike.compiler.generator.entrypoint.EntryPointTypeFunctions
import spike.compiler.generator.entrypoint.EntryPointTypeInternal
import spike.compiler.generator.entrypoint.EntryPointTypeProperties
import spike.compiler.generator.entrypoint.EntryPointTypeSuperinterface
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryFileWithType
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeInternal
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeMethod
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeSuperinterface
import spike.compiler.graph.DependencyGraph

class DependencyGraphGenerator(
    private val environment: SymbolProcessorEnvironment,
) {

    private val resolver = TypeResolver()

    fun generate(graph: DependencyGraph) {
        val dependencyContainerType = GeneratorChainGeneric(
            subject = graph,
            generators = listOf(
                DependencyContainerTypeConstructor(),
                DependencyContainerTypeFactory(),
                DependencyContainerTypeInternal(),
            ),
            resolver = resolver,
            spec = TypeSpec.classBuilder(resolver.getDependencyContainerClassName(graph.entry.type))
        )
        val dependencyContainerFile = GeneratorChainGeneric(
            subject = graph,
            generators = listOf(
                DependencyContainerFileWithType(dependencyContainerType),
            ),
            resolver = resolver,
            spec = FileSpec.builder(resolver.getDependencyContainerClassName(graph.entry.type))
        )
        try {
            dependencyContainerFile.proceed().build().writeTo(environment.codeGenerator, false)
        } catch (_: FileAlreadyExistsException) {
        }

        val entryPointFactoryType = GeneratorChainGeneric(
            subject = graph.entry.factory,
            generators = listOf(
                EntryPointFactoryTypeSuperinterface(),
                EntryPointFactoryTypeInternal(),
                EntryPointFactoryTypeMethod(),
            ),
            resolver = resolver,
            spec = TypeSpec.objectBuilder(resolver.transformClassName(graph.entry.factory.type))
        )
        val entryPointFactoryFile = GeneratorChainGeneric(
            subject = graph.entry.factory,
            generators = listOf(
                EntryPointFactoryFileWithType(entryPointFactoryType),
            ),
            resolver = resolver,
            spec = FileSpec.builder(resolver.transformClassName(graph.entry.factory.type))
        )
        try {
            entryPointFactoryFile.proceed().build().writeTo(environment.codeGenerator, false)
        } catch (_: FileAlreadyExistsException) {
        }

        val entryPointType = GeneratorChainGeneric(
            subject = graph.entry,
            generators = listOf(
                EntryPointTypeInternal(),
                EntryPointTypeSuperinterface(),
                EntryPointTypeConstructor(),
                EntryPointTypeProperties(),
                EntryPointTypeFunctions(),
            ),
            resolver = resolver,
            spec = TypeSpec.classBuilder(resolver.transformClassName(graph.entry.type))
        )
        val entryPointFile = GeneratorChainGeneric(
            subject = graph.entry,
            generators = listOf(
                EntryPointFileWithType(entryPointType),
                EntryPointFileFactoryAccessor(),
                EntryPointFileInitializer(),
            ),
            resolver = resolver,
            spec = FileSpec.builder(resolver.transformClassName(graph.entry.type))
        )
        try {
            entryPointFile.proceed().build().writeTo(environment.codeGenerator, false)
        } catch (_: FileAlreadyExistsException) {
        }
    }
}
