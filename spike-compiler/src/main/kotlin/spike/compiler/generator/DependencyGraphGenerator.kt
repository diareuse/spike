package spike.compiler.generator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.ksp.writeTo
import spike.compiler.generator.container.DependencyContainerFileChain
import spike.compiler.generator.container.DependencyContainerFileWithType
import spike.compiler.generator.container.DependencyContainerTypeChain
import spike.compiler.generator.container.DependencyContainerTypeConstructor
import spike.compiler.generator.container.DependencyContainerTypeFactory
import spike.compiler.generator.container.DependencyContainerTypeInternal
import spike.compiler.generator.entrypoint.EntryPointFileChain
import spike.compiler.generator.entrypoint.EntryPointFileInitializer
import spike.compiler.generator.entrypoint.EntryPointFileWithType
import spike.compiler.generator.entrypoint.EntryPointTypeChain
import spike.compiler.generator.entrypoint.EntryPointTypeConstructor
import spike.compiler.generator.entrypoint.EntryPointTypeFunctions
import spike.compiler.generator.entrypoint.EntryPointTypeInternal
import spike.compiler.generator.entrypoint.EntryPointTypeProperties
import spike.compiler.generator.entrypoint.EntryPointTypeSuperinterface
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryFileChain
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryFileWithType
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeChain
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeInternal
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeMethod
import spike.compiler.generator.entrypoint.factory.EntryPointFactoryTypeSuperinterface
import spike.graph.DependencyGraph
import java.util.Map.entry

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
        dependencyContainerFile.proceed().build().writeTo(environment.codeGenerator, false)

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
        entryPointFactoryFile.proceed().build().writeTo(environment.codeGenerator, false)

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
                EntryPointFileInitializer()
            ),
            resolver = resolver
        )
        entryPointFile.proceed().build().writeTo(environment.codeGenerator, false)
    }

}