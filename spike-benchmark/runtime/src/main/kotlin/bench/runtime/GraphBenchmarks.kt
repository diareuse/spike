package bench.runtime

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.Mode
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

@State(Scope.Benchmark)
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
    fun kotlinInject() = bench.kotlininject.createGraph()

    @Benchmark
    fun koin() = bench.koin.createGraph()

}

@State(Scope.Benchmark)
class GraphBenchmarkResolve {

    lateinit var manual: bench.manual.Graph
    lateinit var spike: bench.spike.Graph
    lateinit var dagger: bench.dagger.Graph
    lateinit var metro: bench.metro.Graph
    lateinit var kotlinInject: bench.kotlininject.Graph
    lateinit var koin: bench.koin.Graph

    @Setup
    fun createGraphs() {
        manual = bench.manual.createGraph()
        spike = bench.spike.createGraph()
        dagger = bench.dagger.createGraph()
        metro = bench.metro.createGraph()
        kotlinInject = bench.kotlininject.createGraph()
        koin = bench.koin.createGraph()
    }

    @Benchmark
    fun manual() = bench.manual.resolve(manual)

    @Benchmark
    fun spike() = bench.spike.resolve(spike)

    @Benchmark
    fun dagger() = bench.dagger.resolve(dagger)

    @Benchmark
    fun metro() = bench.metro.resolve(metro)

    @Benchmark
    fun kotlinInject() = bench.kotlininject.resolve(kotlinInject)

    @Benchmark
    fun koin() = bench.koin.resolve(koin)

}

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
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
    fun kotlinInject() = bench.kotlininject.resolve()

    @Benchmark
    fun koin() = bench.koin.resolve()

}
