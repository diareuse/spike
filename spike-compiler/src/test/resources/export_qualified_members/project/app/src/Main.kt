fun main() {
    val client = Configured().client
    require(client.primary == "a" && client.secondary == "b" && client.tertiary == "c") {
        "got ${client.primary}, ${client.secondary}, ${client.tertiary}"
    }
}

@spike.EntryPoint
interface Configured {
    val client: Client

    companion object
}

@spike.Include
class Client(@Primary val primary: String, @Secondary val secondary: String, val tertiary: @Tertiary String)
