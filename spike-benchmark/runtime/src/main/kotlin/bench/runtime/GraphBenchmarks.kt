package bench.runtime

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.Mode
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import org.openjdk.jmh.annotations.Fork

@State(Scope.Benchmark)
@Fork(jvmArgsAppend = ["-Xms1g", "-Xmx1g", "-XX:+UseParallelGC"])
class GraphBenchmarkCreate {

    @Benchmark
    fun manual() = bench.manual.createGraph()

    @Benchmark
    fun spike() = bench.spike.createGraph()

    @Benchmark
    fun dagger() = bench.dagger.createGraph()

    @Benchmark
    fun metro() = bench.metro.createGraph()

    @Benchmark
    fun kotlin_inject() = bench.kotlininject.createGraph()

    @Benchmark
    fun koin() = bench.koin.createGraph()

}

@State(Scope.Benchmark)
@Fork(jvmArgsAppend = ["-Xms1g", "-Xmx1g", "-XX:+UseParallelGC"])
class GraphBenchmarkResolve {

    @Benchmark
    fun manual(state: GraphStateManual) = bench.manual.resolve(state.graph)

    @Benchmark
    fun spike(state: GraphStateSpike) = bench.spike.resolve(state.graph)

    @Benchmark
    fun dagger(state: GraphStateDagger) = bench.dagger.resolve(state.graph)

    @Benchmark
    fun metro(state: GraphStateMetro) = bench.metro.resolve(state.graph)

    @Benchmark
    fun kotlin_inject(state: GraphStateKotlinInject) = bench.kotlininject.resolve(state.graph)

    @Benchmark
    fun koin(state: GraphStateKoin) = bench.koin.resolve(state.graph)

}

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
@Fork(jvmArgsAppend = ["-Xms1g", "-Xmx1g", "-XX:+UseParallelGC"])
class GraphBenchmarkColdStart {

    @Benchmark
    fun manual() = bench.manual.resolve()

    @Benchmark
    fun spike() = bench.spike.resolve()

    @Benchmark
    fun dagger() = bench.dagger.resolve()

    @Benchmark
    fun metro() = bench.metro.resolve()

    @Benchmark
    fun kotlin_inject() = bench.kotlininject.resolve()

    @Benchmark
    fun koin() = bench.koin.resolve()

}

@State(Scope.Benchmark)
class GraphStateManual {
    lateinit var graph: bench.manual.Graph

    @Setup
    fun create() {
        graph = bench.manual.createGraph()
    }
}

@State(Scope.Benchmark)
class GraphStateSpike {
    lateinit var graph: bench.spike.Graph

    @Setup
    fun create() {
        graph = bench.spike.createGraph()
    }
}

@State(Scope.Benchmark)
class GraphStateDagger {
    lateinit var graph: bench.dagger.Graph

    @Setup
    fun create() {
        graph = bench.dagger.createGraph()
    }
}

@State(Scope.Benchmark)
class GraphStateMetro {
    lateinit var graph: bench.metro.Graph

    @Setup
    fun create() {
        graph = bench.metro.createGraph()
    }
}

@State(Scope.Benchmark)
class GraphStateKotlinInject {
    lateinit var graph: bench.kotlininject.Graph

    @Setup
    fun create() {
        graph = bench.kotlininject.createGraph()
    }
}

@State(Scope.Benchmark)
class GraphStateKoin {
    lateinit var graph: bench.koin.Graph

    @Setup
    fun create() {
        graph = bench.koin.createGraph()
    }
}
