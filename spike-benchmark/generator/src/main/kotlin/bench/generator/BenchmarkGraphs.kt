package bench.generator

import spike.EntryPoint
import spike.Include
import java.io.File

/**
 * Writes the same dependency graph in every framework's dialect, plus the profiler scenarios that build them.
 */
@Include
class BenchmarkGraphs(
    private val sources: SourceTree,
    dialects: Map<Int, Dialect>,
    private val scenarios: ProfilerScenarios,
) {
    private val dialects = dialects.toSortedMap().values.toList()

    fun writeTo(root: File) {
        for (dialect in dialects) sources.writeTo(root.resolve("graphs/${dialect.id}/src/main/kotlin"), dialect)
        scenarios.writeTo(root.resolve("scenarios.conf"), dialects)
    }
}

@EntryPoint
interface BenchmarkGenerator {
    val graphs: BenchmarkGraphs

    companion object
}

@Include
fun sourceTree(): SourceTree =
    SourceTree(TreeShapeSeeded(size = 2000, maxDependencies = 3, seed = 1).grow(), nodesPerFile = 50)
