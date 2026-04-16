@file:Suppress("DEPRECATION")

package spike.compiler.generator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import spike.compiler.graph.DependencyGraph

class DependencyGraphGenerator(
    private val environment: SymbolProcessorEnvironment,
) {

    private val resolver = TypeResolver()

    fun generate(graph: DependencyGraph) {
        val context = FileGeneratorContext(resolver, graph)
        val dependencyFactoryClassName = context.resolver.peerClass(graph, "Factory")
        val entryPoint = EntryPointGenerator(dependencyFactoryClassName)
        val instructionSet = InstructionSetGenerator()
        val dependencyFactory = DependencyFactoryGenerator(
            dependencyFactoryClassName = dependencyFactoryClassName,
            instructionSet = instructionSet,
            dependencyHolder = { index -> DependencyHolderGenerator(index, dependencyFactoryClassName) },
        )
        dependencyFactory.generate(context, ::writeToFile)
        entryPoint.generate(context, ::writeToFile)
    }

    private fun writeToFile(fileSpec: FileSpec) {
        try {
            fileSpec.writeTo(environment.codeGenerator, false)
        } catch (_: FileAlreadyExistsException) {
            environment.logger.warn("File ${fileSpec.name} already exists")
        }
    }
}
