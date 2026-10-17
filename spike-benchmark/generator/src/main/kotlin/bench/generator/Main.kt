package bench.generator

import java.io.File

fun main(args: Array<String>) {
    BenchmarkGenerator().graphs.writeTo(File(args.single()))
}
