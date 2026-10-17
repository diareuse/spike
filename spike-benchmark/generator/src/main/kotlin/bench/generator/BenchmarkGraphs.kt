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

    @EntryPoint.Factory
    interface Factory {
        fun create(size: GraphSize): BenchmarkGenerator
    }

    companion object
}

/**
 * How many nodes the generated graph has; the benchmark uses [Benchmark], a smoke test far fewer.
 */
@JvmInline
value class GraphSize(val nodes: Int) {
    companion object {
        val Benchmark = GraphSize(2000)
    }
}

@Include
fun sourceTree(size: GraphSize): SourceTree =
    SourceTree(TreeShapeSeeded(size = size.nodes, maxDependencies = 3, seed = 1).grow(), nodesPerFile = 50)
