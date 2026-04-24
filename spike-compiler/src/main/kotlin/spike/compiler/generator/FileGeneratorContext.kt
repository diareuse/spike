package spike.compiler.generator

import com.google.devtools.ksp.symbol.KSFile
import spike.compiler.graph.DependencyGraph
import spike.compiler.graph.TypeFactory

data class FileGeneratorContext(
    val resolver: TypeResolver,
    val graph: DependencyGraph,
    val originatingFiles: List<KSFile>,
    val instructions: DependencyFactoryInstructionsSet = DependencyFactoryInstructionsSet(),
    val ids: TypeFactoryIdHolder = TypeFactoryIdHolder()
) {
    fun getDependencyId(factory: TypeFactory): Int {
        return ids.getOrAdd(factory)
    }
}
