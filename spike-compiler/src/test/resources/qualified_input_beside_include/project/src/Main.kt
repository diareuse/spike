fun main() {
    val graph = Configured.factory().create("given")
    require(graph.client.override == "given") { "qualified input was ${graph.client.override}" }
    require(graph.client.default == "default") { "unqualified binding was ${graph.client.default}" }
}

@spike.Qualifier
annotation class Override

@spike.EntryPoint
interface Configured {
    val client: Client

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(@Override config: String): Configured
    }

    companion object
}

@spike.Include
class Client(@Override val override: String, val default: String)

@spike.Include
fun defaultConfig(): String = "default"
