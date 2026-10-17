package bench.generator

import java.io.File

fun main(args: Array<String>) {
    val size = args.getOrNull(1)?.toInt()?.let(::GraphSize) ?: GraphSize.Benchmark
    BenchmarkGenerator(size).graphs.writeTo(File(args.first()))
}
