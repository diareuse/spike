fun main() {
    val first = PropertyEntryPoint.factory().create("first")
    val second = PropertyEntryPoint.factory().create("second")
    require(first.client === first.client) { "one entry point created its singleton twice" }
    require(first.client !== second.client) { "two entry points shared a singleton" }
    require(second.client.config == "second") { "second entry point got ${second.client.config}" }
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val client: Client

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(config: String): PropertyEntryPoint
    }

    companion object
}

@spike.Singleton
@spike.Include
class Client(val config: String)
