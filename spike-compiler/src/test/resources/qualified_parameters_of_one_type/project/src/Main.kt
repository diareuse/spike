fun main() {
    val client = Configured.factory().create("a", "b").client
    require(client.primary == "a" && client.secondary == "b") { "got ${client.primary}, ${client.secondary}" }
}

@spike.Qualifier
annotation class Primary

@spike.Qualifier
annotation class Secondary

@spike.EntryPoint
interface Configured {
    val client: Client

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(@Primary primary: String, @Secondary secondary: String): Configured
    }

    companion object
}

@spike.Include
class Client(@Primary val primary: String, @Secondary val secondary: String)
