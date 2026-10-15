fun main() {
    val client = Configured.factory().create("a", "b").client
    require(client.primary == "a" && client.plain == "b") { "got ${client.primary}, ${client.plain}" }
}

@spike.EntryPoint
interface Configured {
    val client: Client

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(@Primary primary: String, plain: String): Configured
    }

    companion object
}

@spike.Include
class Client(@Primary val primary: String, val plain: String)
