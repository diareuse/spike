@file:Suppress("DEPRECATION")

package spike.compiler.generator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.ksp.writeTo
import spike.compiler.graph.DependencyGraph

class DependencyGraphGenerator(
    private val environment: SymbolProcessorEnvironment,
) {

    private val resolver = TypeResolver()

    fun generate(graph: DependencyGraph) {
        /*val dependencyContainerType = GeneratorChain.classType(
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
        }*/
        MegaGenerator(graph, resolver).generate().forEach { file ->
            try {
                file.writeTo(environment.codeGenerator, false)
            } catch (_: FileAlreadyExistsException) {
            }
        }
    }
}
