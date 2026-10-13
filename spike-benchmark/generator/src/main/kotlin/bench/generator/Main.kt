package bench.generator

import java.io.File

fun main(args: Array<String>) {
    val root = File(args.single())
    val tree = TreeShapeSeeded(size = 2000, maxDependencies = 3, seed = 1).grow()
    val sources = SourceTree(tree, nodesPerFile = 50)
    val dialects = listOf(
        DialectManual(),
        DialectSpike(),
        DialectDagger(),
        DialectMetro(),
        DialectKotlinInject(),
        DialectKoin()
    )
    for (dialect in dialects) {
        sources.writeTo(root.resolve("graphs/${dialect.id}/src/main/kotlin"), dialect)
    }
    ProfilerScenarios(sources).writeTo(root.resolve("scenarios.conf"), dialects)
}
