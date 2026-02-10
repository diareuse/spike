package spike.compiler.generator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.ksp.writeTo
import spike.compiler.generator.container.*
import spike.compiler.generator.entrypoint.*
import spike.compiler.generator.entrypoint.factory.*
import spike.graph.DependencyGraph

class DependencyGraphGenerator(
    private val environment: SymbolProcessorEnvironment
) {

    private val resolver = TypeResolver()

    fun generate(graph: DependencyGraph) {
        val dependencyContainerType = DependencyContainerTypeChain(
            subject = graph,
            generators = listOf(
                DependencyContainerTypeConstructor(),
                DependencyContainerTypeFactory(),
                DependencyContainerTypeInternal()
            ),
            resolver = resolver
        )
        val dependencyContainerFile = DependencyContainerFileChain(
            subject = graph,
            generators = listOf(
                DependencyContainerFileWithType(dependencyContainerType)
            ),
            resolver = resolver
        )
        try {
            dependencyContainerFile.proceed().build().writeTo(environment.codeGenerator, false)
        } catch (_: FileAlreadyExistsException) {
        }

        val entryPointFactoryType = EntryPointFactoryTypeChain(
            subject = graph.entry.factory,
            generators = listOf(
                EntryPointFactoryTypeSuperinterface(),
                EntryPointFactoryTypeInternal(),
                EntryPointFactoryTypeMethod()
            ),
            resolver = resolver
        )
        val entryPointFactoryFile = EntryPointFactoryFileChain(
            subject = graph.entry.factory,
            generators = listOf(
                EntryPointFactoryFileWithType(entryPointFactoryType)
            ),
            resolver = resolver
        )
        try {
            entryPointFactoryFile.proceed().build().writeTo(environment.codeGenerator, false)
        } catch (_: FileAlreadyExistsException) {
        }

        val entryPointType = EntryPointTypeChain(
            subject = graph.entry,
            generators = listOf(
                EntryPointTypeInternal(),
                EntryPointTypeSuperinterface(),
                EntryPointTypeConstructor(),
                EntryPointTypeProperties(),
                EntryPointTypeFunctions()
            ),
            resolver = resolver
        )
        val entryPointFile = EntryPointFileChain(
            subject = graph.entry,
            generators = listOf(
                EntryPointFileWithType(entryPointType),
                EntryPointFileFactoryAccessor(),
                EntryPointFileInitializer()
            ),
            resolver = resolver
        )
        try {
            entryPointFile.proceed().build().writeTo(environment.codeGenerator, false)
        } catch (_: FileAlreadyExistsException) {
        }
    }

}