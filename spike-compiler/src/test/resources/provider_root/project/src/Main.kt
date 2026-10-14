fun main() {
    val graph = ProviderRoot()
    val provider = graph.a
    check(Created.count == 0) { "Provider built ${Created.count} instances before get()" }
    val first = provider.get()
    val second = provider.get()
    check(first.b.c !== second.b.c)
    check(Created.count == 2)
}

object Created {
    var count = 0
}

@spike.EntryPoint
interface ProviderRoot {
    val a: spike.Provider<A>

    companion object
}

@spike.Include
class A(val b: B)

@spike.Include
class B(val c: C)

@spike.Include
class C {
    init {
        Created.count++
    }
}
