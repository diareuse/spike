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
        val context = FileGeneratorContext(resolver, graph)
        val entryPoint = EntryPointGenerator(context.resolver.peerClass(graph, "Factory"))
        MegaGenerator(context, entryPoint).generate().forEach { file ->
            try {
                file.writeTo(environment.codeGenerator, false)
            } catch (_: FileAlreadyExistsException) {
            }
        }
    }
}
